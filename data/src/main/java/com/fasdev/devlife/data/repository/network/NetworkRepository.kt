package com.fasdev.devlife.data.repository.network

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import kotlinx.coroutines.flow.Flow

interface NetworkRepository
{
    fun getPosts(typeSection: TypeSection, page: Int): Flow<List<Post>>
}