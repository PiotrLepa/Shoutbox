package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoutbox.repository.ShoutboxRepository

class ShoutboxViewModelFactory(
    private val repository: ShoutboxRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoutboxViewModel(repository) as T
    }
}