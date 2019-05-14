package com.example.shoutbox.ui.shoutbox

import androidx.lifecycle.ViewModel
import com.example.shoutbox.api.ShoutboxApi
import timber.log.Timber

class ShoutboxViewModel : ViewModel() {
    
    
    fun getMessages() {
        Timber.d("getMessages: started")
        ShoutboxApi().getMessages().observeForever {
            Timber.d("getMessages: $it")
        }
    }
}
