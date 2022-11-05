package com.example.smarthhome.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_OPEN
import com.example.smarthhome.constants.Constants.ERROR_LOAD_API
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.model.Event
import com.example.smarthhome.model.Status
import com.example.smarthhome.repository.dao.EventsDao
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import com.example.smarthhome.service.EventsHistory
import com.example.smarthhome.ui.`interface`.ResultVerifySensor
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ApiRepository {

    private val listNameSensor: List<String> = listOf("Alarme", "1 - Porta Sala", "2 - Porta Fundo", "3 - Porta Sacada", "4 - Janela")
    private val listNameAutomation: List<String> = listOf("Garagem", "Luz da Sala", "Luz do Quarto", "Luz da Cozinha", "Luz do Banheiro")
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
                val list: List<Status>? = response.body()
                db.statusDAO.deleteAlarmDB()

                if(list!!.isNotEmpty()){
                    for (i in list.indices){
                        db.statusDAO.insertStatusAlarm(list[i].id - 1, list[i].name, list[i].status, listNameSensor[i], if(list[i].status == CMND_API_SENSOR_BYPASS) 1 else 0)
                    }
                }

                alarmCmnd.disableIconsDefault()
                alarmCmnd.updateAllStateAlarm(response.body())
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun verifySensorArm(context: Context, callback: ResultVerifySensor): ArrayList<Int>{
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesHome()
        val list: ArrayList<Int> = arrayListOf()

        getAllResult.enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                response.body()!!.forEach {
                    db.statusDAO.updateStatusAlarm(it.status, if(it.status == CMND_API_SENSOR_BYPASS) 1 else 0, it.name)

                    if(it.status == CMND_API_SENSOR_OPEN){
                        list.add(it.id - 1)
                    }
                }
                callback.finish(ok = true, list)
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
                callback.finish(false, list)
            }
        })
        return list
    }

    fun getCurrentStateAutomation(context: Context){
        val getAllResult: Call<List<Status>> = api.getAlarmService().getAllStatesAutomation()

        getAllResult.enqueue(object: Callback<List<Status>>{
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                val listStatus: List<Status> = response.body()!!

                db.statusDAO.deleteAutomationDB()
                if(listStatus.isNotEmpty()){
                    for (i in listStatus.indices){
                        db.statusDAO.insertAutomationDB(listStatus[i].id, listStatus[i].status, listNameAutomation[i])
                    }
                }
                automationCmnd.disableDefaultView()
                automationCmnd.updateAllStateAutomation(listStatus)
            }
            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
            }
        })
    }

//    fun getAllLog(context: Context, day: String){
//        val getLogAllResult: Call<List<Event>> = api.getAlarmService().getEvents()
//
//        getLogAllResult.enqueue(object: Callback<List<Event>>{
//            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
//                response.body()?.let {
//                    eventsDao.clearAndAddList(it)
//                    val events = mutableListOf<Event>()
//                    val dateNow = LocalDateTime.parse(day, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
//                    dateNow.minusHours(3)
//
//                    eventsHistory.configDateEvent(dateNow.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyy")), true)
//
//                    for (log in it){
//                        val data = LocalDateTime.parse(log.date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
//                        data.minusHours(3)
//
//                        if (data.dayOfMonth == dateNow.dayOfMonth && data.monthValue == dateNow.monthValue){
//                            events.add(log)
//                        }
//                    }
//                    if(events.isEmpty())
//                        eventsHistory.activeNotFoundEvents()
//                    else
//                        eventsHistory.activeWidgetsView()
//                    eventsHistory.getAdapter().refresh(events)
//                }
//            }
//
//            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
//                Toast.makeText(context, ERROR_LOAD_API, Toast.LENGTH_LONG).show()
//                Log.i("Error HTTP", t.toString())
//            }
//        })
//    }

    fun getDayLog(context: Context, date: String){
        val getLogDayResult: Call<List<Event>> = api.getAlarmService().getDayEvents(date.substring(0, 10))

        getLogDayResult.enqueue(object : Callback<List<Event>>{
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                response.body()!!.let {
                    eventsDao.clearAndAddList(it)
                    val events = mutableListOf<Event>()
                    val dateNow = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
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