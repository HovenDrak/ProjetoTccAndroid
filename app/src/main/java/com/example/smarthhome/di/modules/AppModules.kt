package com.example.smarthhome.di.modules

import androidx.room.Room
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.dsl.module

private const val serverURI = "ssl://bbfb08f6f1b84ffebf8b0c4fbbcd0e90.s1.eu.hivemq.cloud:8883"
private const val clientID = "android_client"
private const val NAME_DATABASE = "smarthome.db"

val appModules = module {

    single<MqttAndroidClient> {
            MqttAndroidClient(
                get(),
                serverURI,
                clientID)
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            NAME_DATABASE
        ).allowMainThreadQueries().build()
    }

    single<Alarm>{
        Alarm()
    }

    single<Automation>{
        Automation()
    }

}




