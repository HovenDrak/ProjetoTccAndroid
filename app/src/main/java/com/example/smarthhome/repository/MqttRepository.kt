package com.example.smarthhome.repository

import android.util.Log
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.ERROR_LOAD_STATE_MQTT
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.LIST_TOPIC_AUTOMATION
import com.example.smarthhome.constants.Constants.PASSWORD_MQTT
import com.example.smarthhome.constants.Constants.TAG_MQTT
import com.example.smarthhome.constants.Constants.TOPIC_ALARM
import com.example.smarthhome.constants.Constants.TOPIC_AUTOMATION
import com.example.smarthhome.constants.Constants.USER_MQTT
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.service.Automation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject
import org.koin.java.KoinJavaComponent.inject

class MqttRepository(private val mqttClient: MqttAndroidClient) {

    private val automationCmnd: Automation by inject(Automation::class.java)
    private val alarmCmnd: Alarm by inject(Alarm::class.java)

    fun connectMqtt() {
        mqttClient.setCallback(callbackMqtt)

        try {
            mqttClient.connect(setMqttAuthentication(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Connection success")
                    subscribe("status/$TOPIC_ALARM")
                    subscribe("status/$TOPIC_AUTOMATION")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Connection failure + $exception")
                    connectMqtt()
                }
            })

        } catch (e: MqttException) { e.printStackTrace() }
    }

    fun connectMqtt(topic: String, cmd: String) {
        mqttClient.setCallback(callbackMqtt)

        try {
            mqttClient.connect(setMqttAuthentication(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG_MQTT, "Connection success")
                    subscribe("status/$TOPIC_ALARM")
                    subscribe("status/$TOPIC_AUTOMATION")
                    publish(topic, cmd)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG_MQTT, "Connection failure + $exception")
                }
            })
        } catch (e: MqttException) { e.printStackTrace() }
    }

    private fun setMqttAuthentication(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.userName = USER_MQTT
        options.password = PASSWORD_MQTT.toCharArray()
        return options
    }

    private val callbackMqtt = object: MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            Log.e(TAG_MQTT, "Connection MQTT lost cause: $cause")
            connectMqtt()
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            val msg = message.toString().replace("\"", "")
            Log.d(TAG_MQTT, "Received message: ${message.toString()} from topic: $topic")

            val json = JSONObject(msg)

            when (topic) {
                "status/$TOPIC_AUTOMATION" ->
                    automationCmnd.updateDeviceAutomation(json.getString("device"), json.getString("newState"))

                "status/$TOPIC_ALARM" ->
                    alarmCmnd.updateDeviceAlarm(json.getString("device"), json.getString("newState"))

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

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        if (mqttClient.isConnected)
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

        else
            connectMqtt(topic, msg)
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
