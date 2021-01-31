package com.fasdev.devlife.data.source.shareData

import android.content.SharedPreferences

class SharedData(private val sharedPreferences: SharedPreferences)
{
    companion object {
        const val NAME_SETTINGS = "devlife_shared_data"

        const val KEY_LATEST_ID = "latest_id"
        const val KEY_HOT_ID = "hot_id"
        const val KEY_TOP_ID = "top_id"
    }

    var latestId: Long
        get() {
            return sharedPreferences.getLong(KEY_LATEST_ID, -1)
        }
        set(value) {
            sharedPreferences.edit().putLong(KEY_LATEST_ID, value).apply()
        }

    var hotId: Long
        get() {
            return sharedPreferences.getLong(KEY_HOT_ID, -1)
        }
        set(value) {
            sharedPreferences.edit().putLong(KEY_HOT_ID, value).apply()
        }

    var topId: Long
        get() {
            return sharedPreferences.getLong(KEY_TOP_ID, -1)
        }
        set(value) {
            sharedPreferences.edit().putLong(KEY_TOP_ID, value).apply()
        }
}