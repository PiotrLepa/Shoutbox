package com.example.shoutbox.db

import androidx.room.TypeConverter
import com.example.shoutbox.util.fromJson
import com.google.gson.Gson

object ListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToWeatherList(json: String): List<MessageEntry> {
        return Gson().fromJson<List<MessageEntry>>(json)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(list: List<MessageEntry>): String {
        return Gson().toJson(list)
    }
}