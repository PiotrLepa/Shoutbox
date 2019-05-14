package com.example.shoutbox.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntry(
    val content: String,
    val date: String,
    @PrimaryKey
    val id: String,
    val login: String
)