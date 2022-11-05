package com.example.smarthhome.retrofit.service

import com.example.smarthhome.model.Status
import retrofit2.Call
import com.example.smarthhome.constants.Constants.PATH_API_HOME
import com.example.smarthhome.constants.Constants.PATH_API_AUTOMATION
import com.example.smarthhome.constants.Constants.PATH_API_LOG_ALL
import com.example.smarthhome.constants.Constants.PATH_API_LOG_DAY
import com.example.smarthhome.model.Event
import retrofit2.http.*

interface AlarmService {

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = PATH_API_HOME)
    fun getAllStatesHome(): Call<List<Status>>

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = PATH_API_AUTOMATION)
    fun getAllStatesAutomation(): Call<List<Status>>

    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = PATH_API_LOG_ALL)
    fun getEvents(): Call<List<Event>>

    @Headers("Content-Type: application/json")
    @GET("$PATH_API_LOG_DAY/{day}")
    fun getDayEvents(@Path("day") day: String): Call<List<Event>>

}