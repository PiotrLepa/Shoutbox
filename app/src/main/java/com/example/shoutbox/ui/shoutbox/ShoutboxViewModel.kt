package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shoutbox.repository.ShoutboxRepository
import timber.log.Timber

class ShoutboxViewModel(
    private val repository: ShoutboxRepository
) : ViewModel() {

    private val shouldRefresh = MutableLiveData<Boolean>()

    val messages = Transformations
        .switchMap(shouldRefresh) {
            return@switchMap repository.getMessages()
        }

    init {
        getMessages()
    }

    fun getMessages() {
        Timber.d("getMessages: started")
        shouldRefresh.value = true
    }
}
