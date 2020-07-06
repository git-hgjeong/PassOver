package com.example.passover.appdata

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room

class AppConfigModel(application: Application) : AndroidViewModel(application){
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "AppConfigData"
    ).build()

    fun get(): LiveData<AppConfigData> {
        return db.appConfigDao().get()
    }

    suspend fun insert(appData : AppConfigData){
        db.appConfigDao().insert(appData)
    }
}