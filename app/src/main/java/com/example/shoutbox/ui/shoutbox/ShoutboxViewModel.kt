package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutbox.db.MessageInput
import com.example.shoutbox.repository.ShoutboxRepository
import kotlinx.coroutines.launch

class ShoutboxViewModel(
    private val repository: ShoutboxRepository
) : ViewModel() {

    val messages = repository.messages


    init {
        refreshMessages()
    }

    fun refreshMessages() {
        viewModelScope.launch {
            repository.getMessages()
        }
    }

    fun sendMessage(message: MessageInput) {
        viewModelScope.launch {
            repository.sendMessage(message)
        }
    }

    fun updateMessage(messageInput: MessageInput, id: String) {
        viewModelScope.launch {
            repository.updateMessage(messageInput, id)
        }
    }

    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }
}
