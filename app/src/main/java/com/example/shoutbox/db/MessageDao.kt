package com.example.shoutbox.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messagesList: List<MessageEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntry)

    @Query("SELECT * FROM messages ORDER BY date")
    fun getMessages(): LiveData<List<MessageEntry>>

    @Query("DELETE FROM messages WHERE id = :id")
    fun deleteMessage(id: String)
}