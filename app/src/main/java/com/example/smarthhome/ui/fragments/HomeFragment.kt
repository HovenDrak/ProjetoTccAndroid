package com.example.smarthhome.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Alarm
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.schedule
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM


class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mqttClient: MqttAndroidClient by inject()
    private val alarmCmnd: Alarm by inject()

    private val mqttRepository = MqttRepository(mqttClient)
    private val apiRepository = ApiRepository()
    private var show = false

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        alarmCmnd.setConfigAlarm(binding, context!!)
        mqttConfig()
        configButtons()
        apiRepository.getCurrentStateHome(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttRepository.unsubscribeTopics(LIST_TOPIC_ALARM)
        _binding = null
    }

    private fun mqttConfig() {
        mqttRepository.setFragmentCallback(0)
        mqttRepository.subscribeTopics(LIST_TOPIC_ALARM)
    }

    private fun configButtons() {
        configButtonDesarm()
        configButtonArm()
        configButtonVioled()
        configButtonActiveDesarm()
        configButtonActiveArm()
    }

    private fun configButtonActiveDesarm() {
        binding.btnActiveDesarm.setOnClickListener{
            if(show) {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                show = false
                mqttRepository.publish("cmnd/alarme", "\"desarmado\"")
                Toast.makeText(context, "Comando de Desarme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener{
            if(show) {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                show = false
                mqttRepository.publish("cmnd/alarme", "\"armado\"")
                Toast.makeText(context, "Comando de Arme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun configButtonVioled(){
        binding.btnVioled.setOnClickListener{ show = configAnimation() }
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener{ show = configAnimation() }
    }

    private fun configButtonDesarm() {
        binding.btnDesarm.setOnClickListener{ show = configAnimation() }
    }

    private fun configAnimation() =
        if (!show) {
        runAnimation(R.anim.animation_down, R.anim.animation_move_down, View.VISIBLE)
        true
        } else {
            runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
            false
        }

    private fun runAnimation(animationCommand: Int, animationStatus: Int, visible: Int) {
        binding.materialCardViewComand
            .startAnimation(AnimationUtils.loadAnimation(context, animationCommand))
        binding.materialCardViewComand
            .visibility = visible
        binding.materialCardViewSensors
            .startAnimation(AnimationUtils.loadAnimation(context, animationStatus))
    }
}