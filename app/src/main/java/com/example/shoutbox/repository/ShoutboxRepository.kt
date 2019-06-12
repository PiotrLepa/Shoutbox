package com.example.shoutbox.repository

import com.example.shoutbox.api.ShoutboxApi
import com.example.shoutbox.db.MessageDao
import com.example.shoutbox.db.model.MessagePost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ShoutboxRepository(
    private val apiService: ShoutboxApi,
    private val messagesDao: MessageDao
) {

    val messages by lazy { messagesDao.getMessages() }

    suspend fun getMessages() {
        withContext(Dispatchers.IO) {
            val response = apiService.getMessages().await()
            Timber.d("getMessages: response size: ${response.size}")
            messagesDao.deleteOldMessages()
            messagesDao.insertMessages(response)
        }
    }

    suspend fun sendMessage(message: MessagePost) {
        withContext(Dispatchers.IO) {
            val response = apiService.sendMessage(message).await()
            messagesDao.insertMessage(response)
        }
    }

    suspend fun updateMessage(messagePost: MessagePost, id: String) {
        withContext(Dispatchers.IO) {
            val response = apiService.updateMessage(messagePost, id).await()
            messagesDao.insertMessage(response)
        }
    }

    suspend fun deleteMessage(messageId : String) {
        withContext(Dispatchers.IO) {
            val response = apiService.deleteMessage(messageId).await()
            messagesDao.deleteMessage(messageId)
        }
    }
}