package com.example.smarthhome.repository

import android.content.Context
import android.widget.Toast
import com.example.smarthhome.model.Status
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {
    private val alarmCmnd: Alarm by inject(Alarm::class.java)
    private val api = ServiceBuilderApi()

    fun getCurrentState(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStates()

        getAllResult.enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                alarmCmnd.disableDefault()
                alarmCmnd.updateAllStateAlarm(response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, "ERRO AO CARREGAR STATUS",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}