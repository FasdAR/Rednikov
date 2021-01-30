package com.fasdev.devlife.data.source.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.fasdev.devlife.data.source.room.model.PostDB
import com.fasdev.devlife.data.source.room.model.QueueDB

data class QueuePostRelation
(
    @Embedded val queue: QueueDB,

    @Relation(
        parentColumn = "id_post",
        entityColumn = "id"
    )
    val post: PostDB
)