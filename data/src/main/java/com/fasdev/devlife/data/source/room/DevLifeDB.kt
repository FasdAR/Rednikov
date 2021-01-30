package com.fasdev.devlife.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fasdev.devlife.data.source.room.DevLifeDB.Companion.VERSION
import com.fasdev.devlife.data.source.room.dao.PostDao
import com.fasdev.devlife.data.source.room.dao.PostQueueDao
import com.fasdev.devlife.data.source.room.dao.QueueDao
import com.fasdev.devlife.data.source.room.model.PostDB
import com.fasdev.devlife.data.source.room.model.QueueDB

@Database(entities = [PostDB::class, QueueDB::class], version = VERSION)
abstract class DevLifeDB: RoomDatabase()
{
    companion object {
        const val VERSION = 1
        const val DB_NAME = "dev_life"

        const val POST_TABLE = "post"
        const val QUEUE_TABLE = "queue"
    }

    abstract fun getPostDao(): PostDao
    abstract fun getQueueDao(): QueueDao
    abstract fun getPostQueueDao(): PostQueueDao
}