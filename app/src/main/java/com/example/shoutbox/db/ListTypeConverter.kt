package com.example.shoutbox.db

import androidx.room.TypeConverter
import com.example.shoutbox.util.fromJson
import com.google.gson.Gson
import org.joda.time.DateTime

object ListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToMessagesList(json: String): List<MessageEntry> {
        return Gson().fromJson<List<MessageEntry>>(json)
    }

    @TypeConverter
    @JvmStatic
    fun messagesListToString(list: List<MessageEntry>): String {
        return Gson().toJson(list)
    }
}