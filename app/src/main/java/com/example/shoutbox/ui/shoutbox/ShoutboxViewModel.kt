package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutbox.db.model.Message
import com.example.shoutbox.util.NoConnectivityException
import com.example.shoutbox.db.model.MessagePost
import com.example.shoutbox.repository.ShoutboxRepository
import com.example.shoutbox.ui.shoutbox.recyclerView.MessageItem
import com.example.shoutbox.util.PreferenceProvider
import com.example.shoutbox.util.EmptyContentException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ShoutboxViewModel(
    private val repository: ShoutboxRepository,
    private val prefProvider: PreferenceProvider
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbar = MutableLiveData<String>()
    val snackbar: LiveData<String>
        get() = _snackbar

    val messages = repository.messages

    init {
        setupMessagesAutoRefresh()
    }

    fun refreshMessages() {
        launchDataChange {
            repository.getMessages()
        }
    }

    fun onSendButtonClicked(message: String) {
        launchDataChange{
            val userName = prefProvider.getUserName()
            if (userName != null && message.isNotBlank()) {
                repository.sendMessage(MessagePost(message, userName))
            } else {
                throw EmptyContentException()
            }
        }
    }

    fun onEditButtonClicked(newContent: String, oldMessage: Message) {
        launchDataChange {
            if (newContent.isNotBlank()) {
                val messageInput = MessagePost(newContent, oldMessage.login)
                repository.updateMessage(messageInput, oldMessage.id)
            } else {
                throw EmptyContentException()
            }
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
                _snackbar.value = "No internet connection."
            } catch (e: EmptyContentException) {
                Timber.e("launchDataChange: EmptyContentException: $e")
                _snackbar.value = "Content cannot be empty"
            } catch (e: Throwable) {
                Timber.e("launchDataChange: Throwable: $e")
                _snackbar.value = "${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setupMessagesAutoRefresh() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Timber.d("setupMessagesAutoRefresh run: called")
                launchDataChange {
                    repository.getMessages()
                }
            }
        }, 0, 10000)
    }
}
