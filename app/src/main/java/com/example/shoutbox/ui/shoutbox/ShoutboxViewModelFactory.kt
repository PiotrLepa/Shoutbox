package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoutbox.repository.ShoutboxRepository
import com.example.shoutbox.util.PreferenceProvider

class ShoutboxViewModelFactory(
    private val repository: ShoutboxRepository,
    private val preferenceProvider: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoutboxViewModel(repository, preferenceProvider) as T
    }
}