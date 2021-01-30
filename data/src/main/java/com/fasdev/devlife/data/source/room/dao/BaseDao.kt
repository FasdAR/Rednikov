package com.fasdev.devlife.data.source.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDao<T>
{
    @Update
    fun update(entity: T)

    @Update
    fun update(entity: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entity: List<T>)

    @Insert
    fun insert(entity: T)

    @Insert
    fun insert(entity: List<T>)
}