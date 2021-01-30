package com.fasdev.devlife.data.repository.local

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import kotlinx.coroutines.flow.Flow

interface LocalRepository
{
    fun savePost(typeSection: TypeSection, post: Post)
    fun getNextPost(typeSection: TypeSection, prevPostId: Long): Flow<Post>
}