package com.fasdev.devloperlife.ui.fragment.post.viewModel

import com.fasdev.devlife.core.common.model.Post
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devloperlife.ui.mvi.ViewEvent

sealed class PostEvent: ViewEvent
{
    data class SetTypeSection(val typeSection: TypeSection): PostEvent()
    object GetNextPost: PostEvent()
    object GetBackPost: PostEvent()
    object ReloadPost: PostEvent()
    object ImageErrorLoad: PostEvent()
    object ImageLoaded: PostEvent()
}