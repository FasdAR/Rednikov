package com.fasdev.devlife.data.repository.network

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devlife.data.source.retrofit.api.DevLifeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkRepositoryImpl(private val devLifeApi: DevLifeApi): NetworkRepository
{
    override fun getPosts(typeSection: TypeSection, page: Int): Flow<List<Post>> = flow {
        emit(devLifeApi.getPosts(typeSection.type, page).result)
    }
}