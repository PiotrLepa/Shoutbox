package com.example.shoutbox.repository

import com.example.shoutbox.api.ShoutboxApi

class ShoutboxRepository {

    fun getMessages() = ShoutboxApi().getMessages()
}