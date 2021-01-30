package com.fasdev.devloperlife.app.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.kodein.di.*

public inline fun <reified VMS: ViewModelStoreOwner, reified V: ViewModel,
        reified VMF: ViewModelProvider.Factory> DI.Builder.bindViewModel() {
    bind<V>() with contexted<VMS>().provider {
        ViewModelProvider(context, instance<VMF>()).get(V::class.java)
    }
}