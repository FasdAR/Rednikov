package com.fasdev.devloperlife.ui.fragment.post.viewModel

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devloperlife.ui.mvi.ViewState

sealed class PostState: ViewState
{
    object Loaded: PostState()
    object UnknownHost: PostState()
    object NullPost: PostState()
    data class SetPost(val isLatest: Boolean, val post: Post): PostState()
}