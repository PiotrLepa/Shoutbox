package com.example.shoutbox.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "messages")
@Parcelize
data class Message(
    val content: String,
    val date: String,
    @PrimaryKey
    val id: String,
    val login: String
) : Parcelable