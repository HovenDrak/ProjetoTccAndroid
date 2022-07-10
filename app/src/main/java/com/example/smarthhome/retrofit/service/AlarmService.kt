package com.example.smarthhome.retrofit.service

import com.example.smarthhome.model.Status
import retrofit2.Call
import retrofit2.http.HTTP
import retrofit2.http.Headers
import com.example.smarthhome.constants.Constants.PATH_API_HOME
import com.example.smarthhome.constants.Constants.PATH_API_AUTOMATION

interface AlarmService {

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = PATH_API_HOME)
    fun getAllStatesHome(): Call<List<Status>>

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = PATH_API_AUTOMATION)
    fun getAllStatesAutomation(): Call<List<Status>>
}