package com.example.smarthhome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.smarthhome.CallbackAlarmApi
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.model.Status
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Alarm
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.schedule


class HomeFragment: Fragment(), CallbackAlarmApi {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val apiRepository = ApiRepository()
    private val alarmCmnd: Alarm by inject()
    private var show = false

    private val mqttClient: MqttAndroidClient by inject()
    private val mqttRepository = MqttRepository(mqttClient)

    private val topicsAlarm = mutableListOf("alarme", "setor1", "setor2", "setor3", "setor4")
    private var countMessage = 0

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        apiRepository.callbackAlarmApi = this
        mqttConfig()
        apiRepository.getCurrentState(requireContext())
        configButtonDesarm()
        configButtonArm()
        configButtonVioled()
        configButtonActiveDesarm()
        configButtonActiveArm()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttRepository.unsubscribeTopics(topicsAlarm)
        _binding = null
    }

    private fun mqttConfig() {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                countMessage += 1
                Log.d(
                    "MQTT",
                    "Received message ${countMessage}: ${message.toString()} from topic: $topic"
                )
                val msg = message.toString()
                when (topic) {
                    "status/${topicsAlarm[0]}" -> alarmCmnd.updateStateAlarme(msg, binding)
                    "status/${topicsAlarm[1]}" -> alarmCmnd.updateStateSensor(1, msg, binding)
                    "status/${topicsAlarm[2]}" -> alarmCmnd.updateStateSensor(2, msg, binding)
                    "status/${topicsAlarm[3]}" -> alarmCmnd.updateStateSensor(3, msg, binding)
                    "status/${topicsAlarm[4]}" -> alarmCmnd.updateStateSensor(4, msg, binding)
                    else -> Log.d("MQTT", "Não foi possivel processar o STATUS")
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                TODO("Not yet implemented")
            }
        })
        mqttRepository.subscribeTopics(topicsAlarm)
    }

    private fun configButtonActiveDesarm() {
        binding.btnActiveDesarm.setOnClickListener{
            if(show) {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                show = false
                Timer().schedule(700) {}
                mqttRepository.publish("cmnd/alarme", "\"desarmado\"")
            }
        }
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener{
            if(show) {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                show = false
                Timer().schedule(700) {}
                mqttRepository.publish("cmnd/alarme", "\"armado\"")
            }
        }
    }
    private fun configButtonVioled(){
        binding.btnVioled.setOnClickListener{
            show = configAnimation()
        }
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener{
            show = configAnimation()
        }
    }

    private fun configButtonDesarm() {
        binding.btnDesarm.setOnClickListener{
            show = configAnimation()
        }
    }

    private fun configAnimation() = if (!show) {
        runAnimation(R.anim.animation_down, R.anim.animation_move_down, View.VISIBLE)
        true
    } else {
        runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
        false
    }

    private fun runAnimation(animationCommand: Int, animationStatus: Int, visible: Int) {
        binding
            .materialCardViewComand
            .startAnimation(AnimationUtils
                .loadAnimation(context, animationCommand))
        binding
            .materialCardViewComand
            .visibility = visible
        binding
            .materialCardViewSensors
            .startAnimation(AnimationUtils
                .loadAnimation(context, animationStatus))
    }

    override fun reveiverApi(list: List<Status>?) {
        Log.i("Receiver API", "chegou mensagem no receiberAPI")
        alarmCmnd.disableDefault(binding)
        alarmCmnd.updateAllStateAlarm(list, binding)
    }

}