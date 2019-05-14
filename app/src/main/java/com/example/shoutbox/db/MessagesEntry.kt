package com.example.shoutbox.db


data class MessagesEntry(
    val content: String,
    val date: String,
    val id: String,
    val login: String
)