package com.fasdev.devloperlife.ui.mvi

interface MviView<S: ViewState> {
    fun render(state: S)
}