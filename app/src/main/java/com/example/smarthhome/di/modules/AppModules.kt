package com.example.smarthhome.di.modules

import androidx.room.Room
import com.example.smarthhome.constants.Constants.CLIENTID_MQTT
import com.example.smarthhome.constants.Constants.HOST_MQTT
import com.example.smarthhome.constants.Constants.NAME_DATABASE
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.dsl.module

val appModules = module {

    single<MqttAndroidClient> {
            MqttAndroidClient(
                get(),
                HOST_MQTT,
                CLIENTID_MQTT)
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




