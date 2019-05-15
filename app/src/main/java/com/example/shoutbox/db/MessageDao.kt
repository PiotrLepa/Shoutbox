package com.example.shoutbox.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messagesList: List<MessageEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntry)

    @Query("SELECT * FROM messages")
    fun getMessages(): LiveData<List<MessageEntry>>

    @Query("SELECT * FROM messages WHERE id = :id")
    fun getMessage(id: String): LiveData<MessageEntry>
}