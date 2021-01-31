package com.fasdev.devloperlife.ui.fragment.post.viewModel

import androidx.lifecycle.*
import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.repository.local.LocalRepository
import com.fasdev.devlife.data.repository.network.NetworkRepository
import com.fasdev.devloperlife.ui.fragment.post.useCase.PostUseCase
import com.fasdev.devloperlife.ui.mvi.MviModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PostViewModel(private var postCase: PostUseCase): ViewModel(), MviModel<PostState, PostEvent>
{
    class Factory(private var postCase: PostUseCase): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostViewModel(postCase) as T
        }
    }

    private var currentState: PostState? = null
    override val state: MutableLiveData<PostState> = MutableLiveData()

    private var typeSection: TypeSection? = null
        set(value) {
            if (field == null) {
                field = value
                value?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        postCase.getCurrentIdPost(it)
                        handleEvent(PostEvent.ReloadPost)
                    }
                }
            }
            else {
                handleEvent(PostEvent.ReloadPost)
            }
        }

    private fun updateState(newState: PostState) {
        currentState = newState
        state.postValue(currentState)
    }

    override fun handleEvent(event: PostEvent)
    {
        when (event)
        {
            is PostEvent.SetTypeSection -> {
                typeSection = event.typeSection
            }
            PostEvent.GetNextPost -> {
                startPostQuery { typeSection ->
                    postCase.getNextPost(typeSection)
                            .wrapPostQuery()
                            .collect {
                                if (it == null) {
                                    updateState(PostState.NullPost)
                                }
                                else {
                                    updateState(PostState.PostInfo(isLoaded = true,
                                            isLoadedImage = false, isLatest = isLatestPost(typeSection),
                                            description = it.description, gifUrl = it.gifURL))
                                }
                            }
                }
            }
            PostEvent.GetBackPost -> {
                startPostQuery { typeSection ->
                    postCase.getBackPost(typeSection)
                            .wrapPostQuery()
                            .collect {
                                if (it == null) {
                                    updateState(PostState.NullPost)
                                }
                                else {
                                    updateState(PostState.PostInfo(isLoaded = true,
                                            isLoadedImage = false, isLatest = isLatestPost(typeSection),
                                            description = it.description, gifUrl = it.gifURL))
                                }
                            }
                }
            }
            PostEvent.ReloadPost -> {
                startPostQuery { typeSection ->
                    postCase.reloadPost(typeSection)
                            .wrapPostQuery()
                            .collect {
                                if (it == null) {
                                    updateState(PostState.NullPost)
                                }
                                else {
                                    updateState(PostState.PostInfo(isLoaded = true,
                                            isLoadedImage = false, isLatest = isLatestPost(typeSection),
                                            description = it.description, gifUrl = it.gifURL))
                                }
                            }
                }
            }
            is PostEvent.ImageLoaded -> {
                val state = currentState as PostState.PostInfo
                updateState(state.copy(isLoadedImage = true))
            }
            PostEvent.ImageErrorLoad -> {
                updateState(PostState.UnknownHost)
            }
        }
    }

    private fun startPostQuery(unit: suspend (typeSection: TypeSection) -> Unit) {
        viewModelScope.launch {
            typeSection?.let { unit(it) }
        }
    }

    private fun <T> Flow<T>.wrapPostQuery(): Flow<T> {
        return this
                .flowOn(Dispatchers.IO)
                .onStart {
                    state.postValue(PostState.PostInfo(isLoaded = false))
                }
                .catch {
                    ex -> handleException(ex)
                }
    }

    private fun handleException(ex: Throwable) {
        when (ex)
        {
            is UnknownHostException -> {
                state.postValue(PostState.UnknownHost)
            }
        }
    }

    private suspend fun isLatestPost(typeSection: TypeSection): Boolean {
        return postCase.isLastPost(typeSection)
                .flowOn(Dispatchers.IO)
                .first()
    }
}