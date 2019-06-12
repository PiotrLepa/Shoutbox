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
import com.example.shoutbox.util.WrongLoginException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ShoutboxViewModel(
    private val repository: ShoutboxRepository,
    private val prefProvider: PreferenceProvider
) : ViewModel() {

    private var timer = Timer()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbar = MutableLiveData<String>()
    val snackbar: LiveData<String>
        get() = _snackbar

    val messages = repository.messages

    init {
        refreshMessages()
    }

    fun onFragmentStart() {
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
        Timber.d("onEditButtonClicked started")
        launchDataChange {
            if (newContent.isNotBlank()) {
                if (canEditOrDeleteMessage(oldMessage)) {
                    val messageInput = MessagePost(newContent, oldMessage.login)
                    repository.updateMessage(messageInput, oldMessage.id)
                } else {
                    throw WrongLoginException()
                }
            } else {
                throw EmptyContentException()
            }
        }
    }

    fun onDeleteButtonClicked(clickedItem: MessageItem) {
        Timber.d("onDeleteButtonClicked started")
        launchDataChange {
            if (canEditOrDeleteMessage(clickedItem.message)) {
                repository.deleteMessage(clickedItem.message.id)
            } else {
                throw WrongLoginException()
            }
        }
    }

    private fun canEditOrDeleteMessage(message: Message): Boolean {
        return prefProvider.getUserName() == message.login
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
                _snackbar.value = "Content can not be empty"
            } catch (e: WrongLoginException) {
                Timber.e("launchDataChange: WrongLoginException: $e")
                _snackbar.value = "You can only change your messages"
            } catch (e: Throwable) {
                Timber.e("launchDataChange: Throwable: $e")
                _snackbar.value = "${e.message}"
            } finally {
                _isLoading.value = false
                _snackbar.value = ""
            }
        }
    }

    private fun setupMessagesAutoRefresh() {
        Timber.d("setupMessagesAutoRefresh: started")
        timer.cancel()
        timer = Timer()
        val refreshDelay = prefProvider.getAutoRefreshDelayInMillis() ?: return
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Timber.d("setupMessagesAutoRefresh run: called")
                launchDataChange {
                    repository.getMessages()
                }
            }
        }, refreshDelay, refreshDelay)
    }
}
