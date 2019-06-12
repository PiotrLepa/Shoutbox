package com.example.shoutbox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoutbox.db.model.Message

@Database(
    entities = [
        Message::class],
    version = 1,
    exportSchema = false)
@TypeConverters(ListTypeConverter::class)
abstract class ShoutboxDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var instance: ShoutboxDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ShoutboxDatabase::class.java, "shoutbox_databse")
                .build()
    }
}