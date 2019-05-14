package com.example.shoutbox.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

inline fun <reified T> Gson.fromJson(reader: JsonReader) = this.fromJson<T>(reader, object: TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)