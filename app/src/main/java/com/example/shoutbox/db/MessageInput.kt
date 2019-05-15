package com.example.shoutbox.db

import androidx.room.Entity
import androidx.room.PrimaryKey


data class MessageInput(
    val content: String,
    val login: String
)