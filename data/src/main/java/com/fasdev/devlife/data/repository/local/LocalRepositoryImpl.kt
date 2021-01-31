package com.fasdev.devlife.data.repository.local

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.source.room.dao.PostDao
import com.fasdev.devlife.data.source.room.dao.PostQueueDao
import com.fasdev.devlife.data.source.room.dao.QueueDao
import com.fasdev.devlife.data.source.room.model.toPost
import com.fasdev.devlife.data.source.room.model.toPostDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(private val queueDao: QueueDao,
                          private val postDao: PostDao,
                          private val postQueueDao: PostQueueDao): LocalRepository
{
    override fun savePost(typeSection: TypeSection, post: Post) {
        postQueueDao.cachePost(typeSection.type, post.toPostDB())
    }

    override fun savePosts(typeSection: TypeSection, posts: List<Post>) {
        postQueueDao.cachePosts(typeSection.type, posts.map { it.toPostDB() })
    }

    override fun getPost(idPost: Long): Flow<Post?> = flow { emit(postDao.getPost(idPost)?.toPost()) }

    override fun getPositionPost(typeSection: TypeSection, idPost: Long): Flow<Int> = flow {
        emit(queueDao.getIndexCurrentItem(typeSection.type, idPost))
    }

    override fun getNextPost(typeSection: TypeSection, prevIdPost: Long): Flow<Post?> = flow {
        if (prevIdPost == -1L) {
            emit(queueDao.getFirstPost(typeSection.type)?.post?.toPost())
        } else {
            emit(queueDao.getNextPost(typeSection.type, prevIdPost)?.post?.toPost())
        }
    }

    override fun getBackPost(typeSection: TypeSection, prevIdPost: Long): Flow<Post?> = flow {
        emit(queueDao.getBackPost(typeSection.type, prevIdPost)?.post?.toPost())
    }
}