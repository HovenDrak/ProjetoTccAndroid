package com.example.smarthhome.repository

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.model.Status
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmRepository() {

    private val alarmCmnd: Alarm = Alarm()
    private val api = ServiceBuilderApi()

    fun getCurrentState(binding: FragmentHomeBinding, context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStates()
        getAllResult.enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                alarmCmnd.updateState(binding, response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, "ERRO AO CARREGAR STATUS",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}