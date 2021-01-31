package com.fasdev.devlife.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.source.room.DevLifeDB
import com.fasdev.devlife.data.source.room.model.QueueDB
import com.fasdev.devlife.data.source.room.relation.QueuePostRelation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class QueueDao: BaseDao<QueueDB>
{
    @Query("SELECT * FROM ${DevLifeDB.QUEUE_TABLE} WHERE section_type = :typeSection " +
            "AND id > (SELECT id FROM ${DevLifeDB.QUEUE_TABLE} WHERE id_post = :prevPostId) LIMIT 1")
    abstract fun getNextPost(typeSection: String, prevPostId: Long): QueuePostRelation?

    @Query("SELECT * FROM ${DevLifeDB.QUEUE_TABLE} WHERE section_type = :typeSection " +
            "AND id < (SELECT id FROM ${DevLifeDB.QUEUE_TABLE} WHERE id_post = :prevPostId) ORDER BY id DESC LIMIT 1")
    abstract fun getBackPost(typeSection: String, prevPostId: Long): QueuePostRelation?

    @Query("SELECT * FROM ${DevLifeDB.QUEUE_TABLE} WHERE section_type = :typeSection LIMIT 1")
    abstract fun getFirstPost(typeSection: String): QueuePostRelation?

    @Query("SELECT COUNT(*) FROM queue WHERE section_type = :typeSection AND id <= " +
            "(SELECT id FROM queue WHERE id_post = :postId)")
    abstract fun getIndexCurrentItem(typeSection: String, postId: Long): Int
}