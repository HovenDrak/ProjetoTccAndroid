package com.example.smarthhome.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smarthhome.constants.Constants.ERROR_LOAD_API
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.model.Event
import com.example.smarthhome.model.Status
import com.example.smarthhome.repository.dao.EventsDao
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import com.example.smarthhome.service.EventsHistory
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ApiRepository {

    private val eventsHistory: EventsHistory by inject(EventsHistory::class.java)
    private val automationCmnd: Automation by inject(Automation::class.java)
    private val eventsDao: EventsDao by inject(EventsDao::class.java)
    private val db: AppDatabase by inject(AppDatabase::class.java)
    private val alarmCmnd: Alarm by inject(Alarm::class.java)

    private val api = ServiceBuilderApi()

    fun getCurrentStateHome(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesHome()

        getAllResult.enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                alarmCmnd.disableIconsDefault()
                alarmCmnd.updateAllStateAlarm(response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getCurrentStateAutomation(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesAutomation()

        getAllResult.enqueue(object: Callback<List<Status>>{
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                val listStatus: List<Status> = response.body()!!
                for((count, s) in listStatus.withIndex()){
                    db.statusDAO.updateState(s.status, count)
                }
                automationCmnd.disableDefaultView()
                automationCmnd.updateAllStateAutomation(listStatus)
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getDayLog(context: Context, day: String){
        val getLogDayResult: Call<List<Event>> = api.getAlarmService().getEvents()

        getLogDayResult.enqueue(object: Callback<List<Event>>{
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                response.body()?.let {
                    eventsDao.clearAndAddList(it)
                    val events = mutableListOf<Event>()
                    val dateNow = LocalDateTime.parse(day, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                    dateNow.minusHours(3)

                    eventsHistory.configDateEvent(dateNow.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyy")), true)

                    for (log in it){
                        val data = LocalDateTime.parse(log.date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                        data.minusHours(3)

                        if (data.dayOfMonth == dateNow.dayOfMonth && data.monthValue == dateNow.monthValue){
                            events.add(log)
                        }
                    }
                    if(events.isEmpty())
                        eventsHistory.activeNotFoundEvents()
                    else
                        eventsHistory.activeWidgetsView()
                    eventsHistory.getAdapter().refresh(events)
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
                Log.i("Error HTTP", t.toString())
            }
        })
    }
}