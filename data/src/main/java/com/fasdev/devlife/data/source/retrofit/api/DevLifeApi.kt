package com.fasdev.devlife.data.source.retrofit.api

import com.fasdev.devlife.data.source.retrofit.model.PostNetwork
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DevLifeApi
{
    //https://developerslife.ru/latest/0?json=true

    @GET("{type_section}/{page}")
    suspend fun getPosts(
        @Path("type_section") typeSection: String,
        @Path("page") page: String,
        @Query("json") json: Boolean = true
    ): PostNetwork
}