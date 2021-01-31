package com.fasdev.devloperlife.ui.fragment.post.viewModel

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devloperlife.ui.mvi.ViewState

sealed class PostState: ViewState
{
    object UnknownHost: PostState()
    object NullPost: PostState()
    data class PostInfo(
            val isLoaded: Boolean = false,
            val isLoadedImage: Boolean = false,
            val isLatest: Boolean = false,
            val description: String? = null,
            val gifUrl: String? = null): PostState()
}