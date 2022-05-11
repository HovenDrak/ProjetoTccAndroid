package com.example.smarthhome.retrofit.service

import com.example.smarthhome.model.Status
import retrofit2.Call
import retrofit2.http.HTTP
import retrofit2.http.Headers

interface AlarmService {

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = "/status/all/alarm")
    fun getAllStates(): Call<List<Status>>
}