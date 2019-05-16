package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutbox.util.NoConnectivityException
import com.example.shoutbox.db.MessageInput
import com.example.shoutbox.repository.ShoutboxRepository
import com.example.shoutbox.ui.shoutbox.recyclerView.MessageItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ShoutboxViewModel(
    private val repository: ShoutboxRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val messages = repository.messages

    init {
        refreshMessages()
    }

    fun refreshMessages() {
        launchDataChange {
            repository.getMessages()
        }
    }

    fun onSendButtonClicked(message: String, userName: String?) {
        launchDataChange{
            if (userName != null && message.isNotBlank()) {
                repository.sendMessage(MessageInput(message, userName))
            }
        }
    }

    fun onEditButtonClicked(clickedItem: MessageItem) {
        launchDataChange {
            val message = clickedItem.message
            val messageInput = MessageInput("changed", message.login)
            repository.updateMessage(messageInput, message.id)
        }
    }

    fun onDeleteButtonClicked(clickedItem: MessageItem) {
        launchDataChange {
            repository.deleteMessage(clickedItem.message.id)
        }
    }

    private fun launchDataChange(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _isLoading.value = true
                block()
            } catch (e: NoConnectivityException) {
                Timber.e("launchDataChange: NoConnectivityException $e")
            } catch (e: Throwable) {
                Timber.e("launchDataChange: Throwable: $e")
            } finally {
                    _isLoading.value = false
            }
        }
    }
}
