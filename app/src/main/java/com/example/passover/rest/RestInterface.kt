package com.example.passover.rest

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RestInterface {
    @POST("/test/android.php")
    fun saveData(
        @Query("data") data: String,
        @Query("phone") phone: String
    ): Call<RestResult>

}