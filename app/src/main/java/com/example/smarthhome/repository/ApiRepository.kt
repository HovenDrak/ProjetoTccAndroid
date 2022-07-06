package com.example.smarthhome.repository

import android.content.Context
import android.widget.Toast
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.model.Status
import com.example.smarthhome.model.StatusDB
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {
    private val alarmCmnd: Alarm by inject(Alarm::class.java)
    private val automationCmnd: Automation by inject(Automation::class.java)
    private val db: AppDatabase by inject(AppDatabase::class.java)
    private val api = ServiceBuilderApi()

    fun getCurrentStateHome(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesHome()

        getAllResult.enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                alarmCmnd.disableIconsDefault()
                alarmCmnd.updateAllStateAlarm(response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, "ERRO AO CARREGAR STATUS",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getCurrentStateAutomation(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesAutomation()

        getAllResult.enqueue(object: Callback<List<Status>>{
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                val listStatus: List<Status> = response.body()!!
                var count = 0
                for(s in listStatus){
                    db.statusDAO.updateState(s.status, count)
                    count++
                }
                automationCmnd.disableDefaultView()
                automationCmnd.updateAllStateAutomation(response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, "ERRO AO CARREGAR STATUS",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}