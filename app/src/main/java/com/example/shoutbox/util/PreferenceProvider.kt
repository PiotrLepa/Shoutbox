package com.example.shoutbox.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.shoutbox.R
import timber.log.Timber

class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext
    private val prefManager = PreferenceManager.getDefaultSharedPreferences(appContext)
    private val loginPrefKey = appContext.getString(R.string.loginPreferenceKey)

    fun getUserName(): String? {
        return prefManager.getString(loginPrefKey, null)
    }

    fun saveUserName(userName: String) {
        prefManager.edit()
            .putString(loginPrefKey, userName)
            .apply()
    }
}