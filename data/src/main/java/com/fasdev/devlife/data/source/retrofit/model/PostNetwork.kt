package com.fasdev.devlife.data.source.retrofit.model

import com.fasdev.devlife.core.common.model.Post

data class PostNetwork (
    val result: List<Post>,
    val totalCount: Int
)