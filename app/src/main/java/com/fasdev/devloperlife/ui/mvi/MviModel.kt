package com.fasdev.devloperlife.ui.mvi

import androidx.lifecycle.MutableLiveData

interface MviModel<S: ViewState, E: ViewEvent>
{
    val state: MutableLiveData<S>
    fun handleEvent(event: E)
}