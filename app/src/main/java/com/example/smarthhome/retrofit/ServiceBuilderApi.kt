package com.example.smarthhome.retrofit

import com.example.smarthhome.retrofit.service.AlarmService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilderApi{

    private val alarmService: AlarmService

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api-tcc-oficial.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        alarmService = retrofit.create(AlarmService::class.java)
    }

    fun getAlarmService(): AlarmService {
        return alarmService
    }
}

