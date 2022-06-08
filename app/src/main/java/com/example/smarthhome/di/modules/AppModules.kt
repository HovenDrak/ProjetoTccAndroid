package com.example.smarthhome.di.modules

import com.example.smarthhome.service.Alarm
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.dsl.module

private const val serverURI = "ssl://bbfb08f6f1b84ffebf8b0c4fbbcd0e90.s1.eu.hivemq.cloud:8883"
private const val clientID = "android_client"

val appModules = module {
    single<MqttAndroidClient> {
            MqttAndroidClient(
                get(),
                serverURI,
                clientID)
    }
    single<Alarm>{
        Alarm()
    }

}




