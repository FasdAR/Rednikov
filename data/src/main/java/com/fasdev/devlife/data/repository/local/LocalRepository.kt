package com.fasdev.devlife.data.repository.local

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import kotlinx.coroutines.flow.Flow

interface LocalRepository
{
    fun savePost(typeSection: TypeSection, post: Post)
    fun savePosts(typeSection: TypeSection, list: List<Post>)

    fun getPost(idPost: Long): Flow<Post?>
    fun getPositionPost(typeSection: TypeSection, idPost: Long): Flow<Int>

    fun getNextPost(typeSection: TypeSection, prevIdPost: Long): Flow<Post?>
    fun getBackPost(typeSection: TypeSection, prevIdPost: Long): Flow<Post?>

    fun isLastPost(typeSection: TypeSection, postId: Long): Flow<Boolean>

    fun getSavedIdPost(typeSection: TypeSection): Flow<Long>
    fun setSavedIdPost(typeSection: TypeSection, postId: Long)
}