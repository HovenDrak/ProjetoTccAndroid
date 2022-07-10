package com.example.smarthhome.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_DISARM
import com.example.smarthhome.constants.Constants.TOPIC_CMND_ALARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_ARM
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Alarm
import com.google.android.material.card.MaterialCardView
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject


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
                runAnimation(false)
                animationBounce(binding.btnActiveDesarm)
                sendCommand(CMND_MQTT_DISARM)
                animationSendCmnd(false)
                show = false
                Toast.makeText(context, "Comando de Desarme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener{
            if(show) {
                runAnimation(false)
                animationBounce(binding.btnActiveArm)
                sendCommand(CMND_MQTT_ARM)
                animationSendCmnd(true)
                show = false
                Toast.makeText(context, "Comando de Arme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonVioled(){
        binding.btnVioled.setOnClickListener{
            animationBounce(binding.btnVioled)
            show = configAnimation()
        }
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener{
            animationBounce(binding.btnArm)
            show = configAnimation()
        }
    }

    private fun configButtonDisarm() {
        binding.btnDisarm.setOnClickListener{
            animationBounce(binding.btnDisarm)
            show = configAnimation()
        }
    }

    private fun configAnimation() =
        if (!show) {
            runAnimation(true)
            true
        } else {
            runAnimation(false)
            false
        }

    private fun sendCommand(cmnd: String){
        mqttRepository.publish(TOPIC_CMND_ALARM, cmnd)
    }

    private fun runAnimation(visible: Boolean) {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if(visible){
                    binding.constraintCommand.layoutParams.height = (390 * interpolatedTime).toInt()
                } else{
                    binding.constraintCommand.layoutParams.height = (390 - (interpolatedTime * 390)).toInt()
                }
                binding.constraintCommand.requestLayout()
            }
        }
        animation.duration = 500
        binding.constraintCommand.startAnimation(animation)
    }

    private fun animationSendCmnd(cmndArm: Boolean){
        if(cmndArm){
            binding.btnDisarm.startAnimation(alarmCmnd.configAlphaAnimation(false))
        }else{
            if(binding.btnVioled.visibility == View.GONE) {
                binding.btnArm.startAnimation(alarmCmnd.configAlphaAnimation(false))
            }
        }
    }

    private fun animationBounce(imageButton: ImageButton){
        imageButton.startAnimation(AnimationUtils
            .loadAnimation(this.context, R.anim.bounce))
    }
}