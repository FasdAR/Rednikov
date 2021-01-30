package com.fasdev.devlife.data.source.room.dao

import androidx.room.Dao
import com.fasdev.devlife.data.source.room.model.PostDB

@Dao
abstract class PostDao: BaseDao<PostDB>
{
}