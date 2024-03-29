package com.example.shoutbox.ui.login

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.shoutbox.BaseApplication
import com.example.shoutbox.util.PreferenceProvider
import com.example.shoutbox.util.USER_NAME_SHARED_PREF

class LoginViewModel(
    private val prefProvider: PreferenceProvider
) : ViewModel() {

    private val _isUserSaved = MutableLiveData<Boolean>()
    val isUserSaved: LiveData<Boolean>
        get() = _isUserSaved

    init {
        _isUserSaved.value = isUserSaved()
    }

    fun onLoginButtonClicked(userName: String) {
        saveUserName(userName)
        _isUserSaved.value = true
    }

//    private fun saveUserName(userName: String) {
//        val sharedPref = getApplication<BaseApplication>()
//            .getSharedPreferences(USER_NAME_SHARED_PREF, Context.MODE_PRIVATE)
//        sharedPref.edit {
//            putString(USER_NAME_SHARED_PREF, userName)
//        }
//    }

//    private fun isUserSaved(): Boolean {
//        val sharedPref = getApplication<BaseApplication>()
//            .getSharedPreferences(USER_NAME_SHARED_PREF, Context.MODE_PRIVATE)
//        val userName = sharedPref.getString(USER_NAME_SHARED_PREF, null)
//        return userName != null
//    }

    private fun isUserSaved(): Boolean {
        return prefProvider.getUserName() != null
    }

    private fun saveUserName(userName: String) {
        prefProvider.saveUserName(userName)
    }
}
