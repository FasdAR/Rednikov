package com.fasdev.devlife.data.source.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.fasdev.devlife.data.source.room.DevLifeDB

@Entity(
    tableName = DevLifeDB.QUEUE_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = PostDB::class,
            parentColumns = ["id"],
            childColumns = ["id_post"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class QueueDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "section_type")
    val sectionType: String,
    @ColumnInfo(name = "id_post")
    val idPost: Long,
)