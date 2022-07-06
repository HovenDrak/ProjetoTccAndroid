package com.example.smarthhome.repository

import android.util.Log
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.LIST_TOPIC_LIGHT
import com.example.smarthhome.constants.Constants.PASS_MQTT
import com.example.smarthhome.constants.Constants.TAG_MQTT
import com.example.smarthhome.constants.Constants.USER_MQTT
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class MqttRepository(private val mqttClient: MqttAndroidClient) {

    private val automationCmnd: Automation by inject(Automation::class.java)
    private val alarmCmnd: Alarm by inject(Alarm::class.java)
    private val db: AppDatabase by inject(AppDatabase::class.java)


    private var countMessageAutomation = 0
    private var countMessageAlarm = 0

    fun connectMqtt() {
        try {
            mqttClient.connect(setMqttAuthentication(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Connection success")
                    subscribeInitialTopics()
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
        options.password = PASS_MQTT.toCharArray()
        return options
    }

    private fun subscribeInitialTopics() {
        subscribeTopics(LIST_TOPIC_ALARM)
        subscribe("error/alarme")
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
        unsubscribe("error/alarme")
    }

    fun setFragmentCallback(option: Int){
        when(option){
            0 -> mqttClient.setCallback(callbackAlarm)
            1 -> mqttClient.setCallback(callbackAutomation)
        }
    }

    private val callbackAutomation = object: MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            TODO("Not yet implemented")
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            countMessageAlarm += 1
            Log.d(
                TAG_MQTT, "Received message ${countMessageAlarm}: ${message.toString()} from topic: $topic"
            )
            val msg = message.toString()
            when (topic) {
                "status/${LIST_TOPIC_LIGHT[0]}" -> {
                    automationCmnd.updateStateLight(1, msg)
                    db.statusDAO.updateState(msg.replace("\"", ""), 1)
                }
                "status/${LIST_TOPIC_LIGHT[1]}" -> {
                    automationCmnd.updateStateLight(2, msg)
                    db.statusDAO.updateState(msg.replace("\"", ""), 2)
                }
                "status/${LIST_TOPIC_LIGHT[2]}" -> {
                    automationCmnd.updateStateLight(3, msg)
                    db.statusDAO.updateState(msg.replace("\"", ""), 3)
                }
                "status/${LIST_TOPIC_LIGHT[3]}" -> {
                    automationCmnd.updateStateLight(4, msg)
                    db.statusDAO.updateState(msg.replace("\"", ""), 4)
                }
                else -> Log.d(TAG_MQTT, "Não foi possivel processar o STATUS")
            }
        }
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.i(TAG_MQTT, "$token")
        }
    }

    private val callbackAlarm = object: MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            TODO("Not yet implemented")
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            countMessageAlarm += 1
            Log.d(
                TAG_MQTT, "Received message ${countMessageAlarm}: ${message.toString()} from topic: $topic"
            )
            val msg = message.toString()
            when (topic) {
                "status/${LIST_TOPIC_ALARM[0]}" -> alarmCmnd.updateStateAlarme(msg)
                "status/${LIST_TOPIC_ALARM[1]}" -> alarmCmnd.updateStateSensor(1, msg)
                "status/${LIST_TOPIC_ALARM[2]}" -> alarmCmnd.updateStateSensor(2, msg)
                "status/${LIST_TOPIC_ALARM[3]}" -> alarmCmnd.updateStateSensor(3, msg)
                "status/${LIST_TOPIC_ALARM[4]}" -> alarmCmnd.updateStateSensor(4, msg)
                "error/${LIST_TOPIC_ALARM[0]}" -> {
                    Log.i(TAG_MQTT, "Error alarme: $msg")
                    alarmCmnd.showAlert("Erro ao enviar comando", msg)
                }
                else -> Log.d(TAG_MQTT, "Não foi possivel processar o STATUS")
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
        } catch (e: MqttException) {
            e.printStackTrace()
        }
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
        } catch (e: MqttException) {
            e.printStackTrace()
        }
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
        } catch (e: MqttException) {
            e.printStackTrace()
        }
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
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}
