package com.example.passover.appdata

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppConfigDao {
    @Query("SELECT * FROM AppConfigData")
    fun get(): LiveData<AppConfigData>

    @Insert
    fun insert(appConfig: AppConfigData)

    @Update
    fun update(appConfig: AppConfigData)

}