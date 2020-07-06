package com.example.passover.appdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AppConfigData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appConfigDao(): AppConfigDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "AppConfigData.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}