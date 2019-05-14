package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModel
import com.example.shoutbox.repository.ShoutboxRepository
import timber.log.Timber

class ShoutboxViewModel(
    private val repository: ShoutboxRepository
) : ViewModel() {
    
    
    fun getMessages() {
        Timber.d("getMessages: started")
        repository.getMessages()
    }
}
