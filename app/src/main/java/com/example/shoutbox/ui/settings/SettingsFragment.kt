package com.example.shoutbox.ui.settings

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

import com.example.shoutbox.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var loginPrefKey: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        loginPrefKey = getString(R.string.loginPreferenceKey)

        Timber.d("onCreatePreferences: loginPrefKey: $loginPrefKey")
        val loginPref = findPreference(loginPrefKey) as EditTextPreference
        val userLogin = getUserLogin()
        loginPref.text = userLogin
    }

    private fun getUserLogin(): String? {
        return preferenceManager.sharedPreferences.getString(loginPrefKey, null)
    }
}
