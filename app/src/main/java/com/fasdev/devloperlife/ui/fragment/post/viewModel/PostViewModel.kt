package com.fasdev.devloperlife.ui.fragment.post.viewModel

import androidx.lifecycle.*
import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.repository.local.LocalRepository
import com.fasdev.devlife.data.repository.network.NetworkRepository
import com.fasdev.devloperlife.ui.mvi.MviModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PostViewModel(private var postCase: PostCase): ViewModel(), MviModel<PostState, PostEvent>
{
    class Factory(private var postCase: PostCase): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostViewModel(postCase) as T
        }
    }

    override val state: MutableLiveData<PostState> = MutableLiveData()

    private var typeSection: TypeSection? = null
        set(value) {
            if (field == null) {
                field = value
                value?.let {
                    handleEvent(PostEvent.GetNextPost)
                }
            }
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
                                    state.postValue(PostState.NullPost)
                                }
                                else {
                                    state.postValue(
                                            PostState.SetPost(isLatestPost(typeSection), it)
                                    )
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
                                    state.postValue(PostState.NullPost)
                                }
                                else {
                                    state.postValue(
                                            PostState.SetPost(isLatestPost(typeSection), it)
                                    )
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
                                    state.postValue(PostState.NullPost)
                                }
                                else {
                                    state.postValue(
                                            PostState.SetPost(isLatestPost(typeSection), it)
                                    )
                                }
                            }
                }
            }
            PostEvent.GlideDontLoad -> {
                state.postValue(PostState.UnknownHost)
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
                    state.postValue(PostState.Loaded)
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

class PostCase(private val localRepository: LocalRepository,
               private val networkRepository: NetworkRepository)
{
    companion object {
        const val POST_COUNT = 5
    }

    private var currentIdPost: Long = -1

    private fun calculatePage(indexPost: Int): Int {
        return (indexPost + 1) / POST_COUNT
    }

    private fun loadPosts(typeSection: TypeSection): Flow<List<Post>> =
            localRepository.getPositionPost(typeSection, currentIdPost)
                    .flatMapConcat {
                        networkRepository.getPosts(typeSection, calculatePage(it))
                                .onEach { it ->
                                    localRepository.savePosts(typeSection, it)
                                }
                    }

    private fun loadNextPost(typeSection: TypeSection): Flow<Post?> =
            localRepository.getNextPost(typeSection, currentIdPost)

    fun reloadPost(typeSection: TypeSection): Flow<Post?> {
        return if (currentIdPost == -1L) {
            getNextPost(typeSection)
        } else {
            localRepository.getPost(currentIdPost)
        }
    }

    fun getNextPost(typeSection: TypeSection): Flow<Post?> =
            loadNextPost(typeSection)
                    .flatMapConcat {
                        if (it == null) {
                            loadPosts(typeSection).flatMapConcat {
                                loadNextPost(typeSection)
                            }
                        } else {
                            flowOf(it)
                        }
                    }.onEach {
                        it?.let {
                            currentIdPost = it.id
                        }
                    }

    fun getBackPost(typeSection: TypeSection): Flow<Post?> =
            localRepository.getBackPost(typeSection, currentIdPost)
                    .onEach {
                        it?.let { currentIdPost = it.id }
                    }

    fun isLastPost(typeSection: TypeSection): Flow<Boolean> = localRepository
            .isLastPost(typeSection, currentIdPost)

}