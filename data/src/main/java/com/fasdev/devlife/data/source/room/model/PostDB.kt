package com.fasdev.devlife.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.data.source.room.DevLifeDB

@Entity(
    tableName = DevLifeDB.POST_TABLE
)
data class PostDB(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val description: String,
    @ColumnInfo(name = "gif_url")
    val gifUrl: String
)

fun PostDB.toPost() = Post(id, description, gifUrl)
fun Post.toPostDB() = PostDB(id, description, gifURL)