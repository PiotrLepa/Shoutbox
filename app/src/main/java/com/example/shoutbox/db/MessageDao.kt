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

    @Query("SELECT * FROM messages")
    fun getMessages(): LiveData<List<MessageEntry>>
}