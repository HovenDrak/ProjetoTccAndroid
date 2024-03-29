package com.example.smarthhome.di.modules

import android.view.animation.Animation
import androidx.room.Room
import com.example.smarthhome.constants.Constants.CLIENT_ID_MQTT
import com.example.smarthhome.constants.Constants.HOST_MQTT
import com.example.smarthhome.constants.Constants.NAME_DATABASE
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.repository.dao.EventsDao
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import com.example.smarthhome.service.EventsHistory
import com.example.smarthhome.ui.animation.Animations
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.dsl.module

val appModules = module {

    single<MqttAndroidClient> {
            MqttAndroidClient(
                get(),
                HOST_MQTT,
                CLIENT_ID_MQTT)
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

    single<Animations>{
        Animations()
    }

    single<EventsDao>{
        EventsDao()
    }

    single<EventsHistory>{
        EventsHistory()
    }

}




