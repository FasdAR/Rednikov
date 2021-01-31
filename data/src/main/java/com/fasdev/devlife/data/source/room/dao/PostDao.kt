package com.fasdev.devlife.data.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.fasdev.devlife.data.source.room.DevLifeDB
import com.fasdev.devlife.data.source.room.model.PostDB
import com.fasdev.devlife.data.source.room.model.QueueDB
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostDao: BaseDao<PostDB>
{
    @Query("SELECT * FROM ${DevLifeDB.POST_TABLE} WHERE id = :idPost")
    abstract fun getPost(idPost: Long): PostDB?
}