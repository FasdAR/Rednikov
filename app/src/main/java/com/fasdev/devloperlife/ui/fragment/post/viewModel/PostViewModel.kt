package com.fasdev.devloperlife.ui.fragment.post.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.repository.local.LocalRepository
import com.fasdev.devlife.data.repository.network.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostViewModel(private var postCase: PostCase): ViewModel()
{
    class Factory(private var postCase: PostCase): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostViewModel(postCase) as T
        }
    }

    val currentPost: MutableLiveData<Post> = MutableLiveData()

    fun getNextPost(typeSection: TypeSection) {
        viewModelScope.launch {
            postCase.getNextPost(typeSection)
                    .flowOn(Dispatchers.IO)
                    .catch { ex ->
                        Log.e("EXCEPTION_POST", ex.toString())
                    }
                    .collect {
                        currentPost.postValue(it)
                    }
        }
    }

    fun getBackPost(typeSection: TypeSection) {
        viewModelScope.launch {
            postCase.getBackPost(typeSection)
                    .flowOn(Dispatchers.IO)
                    .catch { ex ->
                        Log.e("EXCEPTION_POST", ex.toString())
                    }
                    .collect {
                        currentPost.postValue(it)
                    }
        }
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
}