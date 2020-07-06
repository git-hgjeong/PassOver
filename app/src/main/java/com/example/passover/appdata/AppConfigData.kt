package com.example.passover.appdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppConfigData(
    var terms : String,
    var policy : String
){
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}