package com.example.smarthhome.repository

import android.util.Log
import com.example.smarthhome.R
import com.example.smarthhome.constants.Constants.BACKGROUND_COLOR_LIGHT_OFF
import com.example.smarthhome.constants.Constants.BACKGROUND_COLOR_LIGHT_ON
import com.example.smarthhome.constants.Constants.CMND_API_GARAGE_CLOSE
import com.example.smarthhome.constants.Constants.CMND_API_GARAGE_OPEN
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.ERROR_LOAD_STATE_MQTT
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.LIST_TOPIC_AUTOMATION
import com.example.smarthhome.constants.Constants.PASSWORD_MQTT
import com.example.smarthhome.constants.Constants.TAG_MQTT
import com.example.smarthhome.constants.Constants.USER_MQTT
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.koin.java.KoinJavaComponent.inject

class MqttRepository(private val mqttClient: MqttAndroidClient) {

    private val automationCmnd: Automation by inject(Automation::class.java)
    private val alarmCmnd: Alarm by inject(Alarm::class.java)
    private val db: AppDatabase by inject(AppDatabase::class.java)

    private var countMessageAutomation = 0
    private var countMessageAlarm = 0

    fun connectMqtt(listTopics: List<String>) {
        try {
            mqttClient.connect(setMqttAuthentication(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Connection success")
                    subscribeTopics(listTopics)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Connection failure + $exception")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun setMqttAuthentication(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.userName = USER_MQTT
        options.password = PASSWORD_MQTT.toCharArray()
        return options
    }

    fun subscribeTopics(topics: List<String>) {
        for(i in topics){
            subscribe("status/$i")
        }
    }

    fun unsubscribeTopics(topics: List<String>) {
        for(i in topics){
            unsubscribe("status/$i")
        }
    }

    fun setFragmentCallback(option: Int){
        when(option){
            0 -> mqttClient.setCallback(callbackAlarm)
            1 -> mqttClient.setCallback(callbackAutomation)
        }
    }

    private val callbackAutomation = object: MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            Log.e(TAG_MQTT, "Connection MQTT lost cause: $cause")
            connectMqtt(LIST_TOPIC_ALARM)
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            countMessageAlarm += 1
            val msg = message.toString().replace("\"", "")
            Log.d(TAG_MQTT, "Received message ${countMessageAutomation}: ${message.toString()} from topic: $topic")
            when (topic) {
                "status/${LIST_TOPIC_AUTOMATION[0]}" -> {
                    automationCmnd.updateStateLight(1, msg)
                    db.statusDAO.updateState(msg, 2)
                }
                "status/${LIST_TOPIC_AUTOMATION[1]}" -> {
                    automationCmnd.updateStateLight(2, msg)
                    db.statusDAO.updateState(msg, 3)
                }
                "status/${LIST_TOPIC_AUTOMATION[2]}" -> {
                    automationCmnd.updateStateLight(3, msg)
                    db.statusDAO.updateState(msg, 4)
                }
                "status/${LIST_TOPIC_AUTOMATION[3]}" -> {
                    automationCmnd.updateStateLight(4, msg)
                    db.statusDAO.updateState(msg, 5)
                }
                "status/${LIST_TOPIC_AUTOMATION[4]}" -> {
                    when(msg) {
                        CMND_API_GARAGE_OPEN -> automationCmnd.updateGarage(R.drawable.ic_garage_open, msg, BACKGROUND_COLOR_LIGHT_ON)
                        CMND_API_GARAGE_CLOSE -> automationCmnd.updateGarage(R.drawable.ic_garage_closed, msg, BACKGROUND_COLOR_LIGHT_OFF)
                    }
                    db.statusDAO.updateState(msg, 1)

                }
                else -> Log.d(TAG_MQTT, ERROR_LOAD_STATE_MQTT)
            }
        }
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.i(TAG_MQTT, "$token")
        }
    }

    private val callbackAlarm = object: MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            Log.e(TAG_MQTT, "Connection MQTT lost cause: $cause")
            connectMqtt(LIST_TOPIC_AUTOMATION)
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            countMessageAlarm += 1
            Log.d(TAG_MQTT, "Received message ${countMessageAlarm}: ${message.toString()} from topic: $topic")
            val msg = message.toString().replace("\"", "")

            when (topic) {
                "status/${LIST_TOPIC_ALARM[0]}" -> alarmCmnd.updateStateAlarm(msg)
                "status/${LIST_TOPIC_ALARM[1]}" -> {
                    alarmCmnd.updateStateSensor(1, msg)
                    db.statusDAO.updateStatusAlarm(msg, if(msg == CMND_API_SENSOR_BYPASS) 1 else 0,1)
                }
                "status/${LIST_TOPIC_ALARM[2]}" -> {
                    alarmCmnd.updateStateSensor(2, msg)
                    db.statusDAO.updateStatusAlarm(msg, if(msg == CMND_API_SENSOR_BYPASS) 1 else 0,2)
                }
                "status/${LIST_TOPIC_ALARM[3]}" -> {
                    alarmCmnd.updateStateSensor(3, msg)
                    db.statusDAO.updateStatusAlarm(msg, if(msg == CMND_API_SENSOR_BYPASS) 1 else 0,3)
                }
                "status/${LIST_TOPIC_ALARM[4]}" -> {
                    alarmCmnd.updateStateSensor(4, msg)
                    db.statusDAO.updateStatusAlarm(msg, if(msg == CMND_API_SENSOR_BYPASS) 1 else 0, 4)
                }
                else -> Log.d(TAG_MQTT, ERROR_LOAD_STATE_MQTT)
            }
        }
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.i(TAG_MQTT, "$token")
        }
    }

    private fun subscribe(topic: String, qos: Int = 1) {
        try {
            if(mqttClient.isConnected) {
                mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG_MQTT, "Subscribed to $topic")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG_MQTT, "Failed to subscribe $topic")
                    }
                })
            }
        } catch (e: MqttException) { e.printStackTrace() }
    }

    private fun unsubscribe(topic: String){
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) { e.printStackTrace() }
    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "$msg published to $topic")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) { e.printStackTrace() }
    }
    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Disconnected")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) { e.printStackTrace() }
    }
}
