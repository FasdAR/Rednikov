package com.fasdev.devloperlife.ui.fragment.post.viewModel

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

    private var currentIdPost: Long = 1

    fun getNextPost(typeSection: TypeSection): Flow<Post> = flow {
        val post = localRepository.getNextPost(typeSection, currentIdPost).first()

        if (post == null) {
            val posPost = localRepository.getPositionPost(typeSection, currentIdPost).first()
            val posts = networkRepository.getPosts(typeSection, calculatePage(posPost)).first()

            localRepository.savePosts(typeSection, posts)

            emit(getNextPost(typeSection).first())
        }
        else {
            currentIdPost = post.id
            emit(post)
        }
    }

    private fun calculatePage(indexPost: Int): Int = (indexPost + 1) / POST_COUNT
}