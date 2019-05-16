package com.example.shoutbox.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}