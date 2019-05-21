package com.example.shoutbox.db

import androidx.room.TypeConverter
import com.example.shoutbox.db.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToMessagesList(json: String): List<Message> {
        return Gson().fromJson<List<Message>>(json)
    }

    @TypeConverter
    @JvmStatic
    fun messagesListToString(list: List<Message>): String {
        return Gson().toJson(list)
    }

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}