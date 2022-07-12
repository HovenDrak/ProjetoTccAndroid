package com.example.smarthhome.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_DISARM
import com.example.smarthhome.constants.Constants.TOPIC_CMND_ALARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_ARM
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.ui.animation.Animations
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject


class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mqttClient: MqttAndroidClient by inject()
    private val anim: Animations by inject()
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
        anim.setConfig(binding, context!!)
        mqttConfig()
        configButtons()
        apiRepository.getCurrentStateHome(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttRepository.unsubscribeTopics(LIST_TOPIC_ALARM)
        _binding = null
        show = false
    }

    override fun onResume() {
        super.onResume()
        show = false
    }

    private fun mqttConfig() {
        mqttRepository.setFragmentCallback(0)
        mqttRepository.subscribeTopics(LIST_TOPIC_ALARM)
    }

    private fun configButtons() {
        configButtonActiveDisarm()
        configButtonActiveArm()
        configButtonVioled()
        configButtonDisarm()
        configButtonArm()
    }

    private fun configButtonActiveDisarm() {
        binding.btnActiveDesarm.setOnClickListener{
            if(show) {
                anim.animationShowCmndButtons(false)
                anim.animationBounce(binding.btnActiveDesarm, false)
                sendCommand(CMND_MQTT_DISARM)
                anim.animationSendCmnd(false)
                show = false
                Toast.makeText(context, "Comando de Desarme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener{
            if(show) {
                anim.animationShowCmndButtons(false)
                anim.animationBounce(binding.btnActiveArm, false)
                sendCommand(CMND_MQTT_ARM)
                anim.animationSendCmnd(true)
                show = false
                Toast.makeText(context, "Comando de Arme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonVioled(){
        binding.btnVioled.setOnClickListener{
            anim.animationBounce(binding.btnVioled, true)
            show = configAnimation()
        }
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener{
            anim.animationBounce(binding.btnArm, false)
            show = configAnimation()
        }
    }

    private fun configButtonDisarm() {
        binding.btnDisarm.setOnClickListener{
            anim.animationBounce(binding.btnDisarm, false)
            show = configAnimation()
        }
    }

    private fun configAnimation() =
        if (!show) {
            anim.animationShowCmndButtons(true)
            true
        } else {
            anim.animationShowCmndButtons(false)
            false
        }

    private fun sendCommand(cmnd: String){
        mqttRepository.publish(TOPIC_CMND_ALARM, cmnd)
    }
}