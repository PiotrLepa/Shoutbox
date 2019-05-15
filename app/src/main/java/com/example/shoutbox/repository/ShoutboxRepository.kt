package com.example.shoutbox.repository

import androidx.lifecycle.LiveData
import com.example.shoutbox.AppExecutors
import com.example.shoutbox.api.ApiResponse
import com.example.shoutbox.api.ShoutboxApi
import com.example.shoutbox.db.MessageDao
import com.example.shoutbox.db.MessageEntry
import com.example.shoutbox.db.MessageInput
import com.example.shoutbox.util.wrapper.Resource
import kotlinx.coroutines.runBlocking

class ShoutboxRepository(
    private val apiService: ShoutboxApi,
    private val messagesDao: MessageDao,
    private val appExecutors: AppExecutors
) {

    fun getMessages(): LiveData<Resource<List<MessageEntry>>> {
        return object : NetworkBoundResource<List<MessageEntry>, List<MessageEntry>>(appExecutors) {
            override fun saveCallResult(item: List<MessageEntry>) {
                return messagesDao.insertMessages(item)
            }

            override fun shouldFetch(data: List<MessageEntry>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<MessageEntry>> {
                return messagesDao.getMessages()
            }

            override fun createCall(): LiveData<ApiResponse<List<MessageEntry>>> {
                return apiService.getMessages()
            }
        }.asLiveData()
    }

    fun sendMessage(message: MessageInput): LiveData<Resource<MessageEntry>> {
        return object : NetworkBoundInputResource<MessageEntry, MessageEntry>(appExecutors) {
            override fun saveCallResult(item: MessageEntry) {
                messagesDao.insertMessage(item)
            }

            override fun loadFromDb(response: MessageEntry): LiveData<MessageEntry> {
                return messagesDao.getMessage(response.id)
            }

            override fun createCall(): LiveData<ApiResponse<MessageEntry>> {
                return apiService.sendMessage(message)
            }
        }.asLiveData()
    }
}