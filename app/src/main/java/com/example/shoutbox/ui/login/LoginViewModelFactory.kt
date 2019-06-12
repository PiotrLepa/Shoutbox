package com.example.shoutbox.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoutbox.util.PreferenceProvider

class LoginViewModelFactory(
    private val prefProvider: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(prefProvider) as T
    }
}