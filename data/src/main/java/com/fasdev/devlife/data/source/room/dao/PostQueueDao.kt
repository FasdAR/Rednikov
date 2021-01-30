package com.fasdev.devlife.data.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.fasdev.devlife.data.source.room.model.PostDB
import com.fasdev.devlife.data.source.room.model.QueueDB

@Dao
abstract class PostQueueDao
{
    @Insert
    abstract fun insertPostDB(entity: PostDB): Long

    @Insert
    abstract fun insertPostDB(entity: List<PostDB>): List<Long>

    @Insert
    abstract fun insertQueueDB(entity: QueueDB)

    @Insert
    abstract fun insertQueueDB(entity: List<QueueDB>)

    @Transaction
    open fun cachePost(typeSection: String, post: PostDB) {
        val idPost = insertPostDB(post)
        insertQueueDB(QueueDB(sectionType = typeSection, idPost = idPost))
    }

    @Transaction
    open fun cachePosts(typeSection: String, posts: List<PostDB>) {
        val ids = insertPostDB(posts)
        val queueDB: MutableList<QueueDB> = mutableListOf()

        ids.forEach {
            queueDB.add(QueueDB(sectionType = typeSection, idPost = it))
        }

        insertQueueDB(queueDB)
    }
}