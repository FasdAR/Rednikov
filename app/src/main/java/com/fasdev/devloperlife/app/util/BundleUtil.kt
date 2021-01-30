package com.fasdev.devloperlife.app.util

import android.os.Bundle

fun <T: Enum<T>> Bundle.putEnum(key: String, value: T?) {
    putInt(key, value?.ordinal ?: -1)
}

inline fun <reified T: Enum<T>> Bundle.getEnum(key: String, default: T) =
    getInt(key).let { if (it >= 0) enumValues<T>()[it] else default }