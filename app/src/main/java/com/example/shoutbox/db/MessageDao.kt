package com.example.shoutbox.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shoutbox.db.model.Message

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messagesList: List<Message>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Query("SELECT * FROM messages ORDER BY date")
    fun getMessages(): LiveData<List<Message>>

    @Query("DELETE FROM messages WHERE id = :id")
    fun deleteMessage(id: String)

    @Query("DELETE FROM messages")
    fun deleteOldMessages()
}