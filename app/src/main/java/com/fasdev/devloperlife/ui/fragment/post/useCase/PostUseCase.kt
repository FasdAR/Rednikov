package com.fasdev.devloperlife.ui.fragment.post.useCase

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.repository.local.LocalRepository
import com.fasdev.devlife.data.repository.network.NetworkRepository
import kotlinx.coroutines.flow.*

class PostUseCase(private val localRepository: LocalRepository,
                  private val networkRepository: NetworkRepository)
{
    companion object {
        const val POST_COUNT = 5
    }

    private var currentIdPost: Long = -1

    suspend fun getCurrentIdPost(typeSection: TypeSection) {
        localRepository.getSavedIdPost(typeSection).collect {
            currentIdPost = it
        }
    }

    fun setCurrentIdPost(typeSection: TypeSection) {
        localRepository.setSavedIdPost(typeSection, currentIdPost)
    }

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
                            setCurrentIdPost(typeSection)
                        }
                    }

    fun getBackPost(typeSection: TypeSection): Flow<Post?> =
            localRepository.getBackPost(typeSection, currentIdPost)
                    .onEach {
                        it?.let {
                            currentIdPost = it.id
                            setCurrentIdPost(typeSection)
                        }
                    }

    fun isLastPost(typeSection: TypeSection): Flow<Boolean> = localRepository
            .isLastPost(typeSection, currentIdPost)
}