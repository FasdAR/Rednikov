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
            "AND id_post > :prevPostId LIMIT 1")
    abstract fun getNextPost(typeSection: String, prevPostId: Long): Flow<QueuePostRelation?>

    @Query("SELECT COUNT(*) FROM ${DevLifeDB.QUEUE_TABLE} WHERE section_type = :typeSection " +
            "AND id_post > :postId")
    abstract fun getIndexCurrentItem(typeSection: String, postId: Long): Flow<Int>
}