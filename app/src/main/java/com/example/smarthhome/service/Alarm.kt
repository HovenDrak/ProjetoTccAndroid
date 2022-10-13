package com.example.smarthhome.service

import android.content.Context
import com.example.smarthhome.databinding.FragmentHomeBinding
import android.view.animation.LinearInterpolator
import android.view.animation.AlphaAnimation
import com.example.smarthhome.model.Status
import com.example.smarthhome.constants.Constants.TAG_MQTT
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_CLOSE
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_OPEN
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_CLOSE
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_OPEN
import com.example.smarthhome.constants.Constants.CMND_SENSOR_DEFAULT
import com.example.smarthhome.constants.Constants.CMND_MQTT_ARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_DISARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_VIOLED
import com.example.smarthhome.constants.Constants.CMND_API_ARM
import com.example.smarthhome.constants.Constants.CMND_API_DISARM
import com.example.smarthhome.constants.Constants.CMND_API_VIOLED
import android.view.animation.Animation
import com.example.smarthhome.R
import android.widget.ImageView
import android.view.View
import android.util.Log
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_BYPASS
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Alarm{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var context: Context
    private var canArm = false

    fun setConfigAlarm(binding: FragmentHomeBinding, context: Context){
        this.binding = binding
        this.context = context
    }

    private fun stateArm() {

        binding.btnDisarm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.btnDisarm.visibility = View.GONE
        binding.txtDesarm.visibility = View.GONE

        binding.btnArm.visibility = View.VISIBLE
        binding.txtArm.visibility = View.VISIBLE

        binding.btnActiveArm.visibility = View.GONE
        binding.txtActiveArm.visibility = View.GONE

        binding.btnActiveDesarm.visibility = View.VISIBLE
        binding.txtActiveDesarm.visibility = View.VISIBLE
    }

    private fun stateDisarm() {

        binding.btnDisarm.visibility = View.VISIBLE
        binding.txtDesarm.visibility = View.VISIBLE

        binding.btnArm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE

        binding.btnVioled.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.btnVioled.visibility = View.GONE
        binding.txtVioled.visibility = View.GONE

        binding.btnActiveArm.visibility = View.VISIBLE
        binding.txtActiveArm.visibility = View.VISIBLE
        binding.btnActiveDesarm.visibility = View.GONE
        binding.txtActiveDesarm.visibility = View.GONE
    }

    private fun stateVioled() {


        binding.btnActiveArm.visibility = View.GONE
        binding.txtActiveArm.visibility = View.GONE

        binding.btnActiveDesarm.visibility = View.VISIBLE
        binding.txtActiveDesarm.visibility = View.VISIBLE

        binding.btnArm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE

        binding.btnVioled.visibility = View.VISIBLE
        binding.txtVioled.visibility = View.VISIBLE

        binding.btnVioled.startAnimation(configAlphaAnimation(true))
    }

     fun disableIconsDefault() {
        binding.progressBarAlarm.visibility = View.GONE
        binding.btnDefault.visibility = View.GONE
        binding.txtDefault.visibility = View.GONE
    }

    fun enableIconsDefault(){
        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE

        binding.btnDisarm.visibility = View.GONE
        binding.txtDesarm.visibility = View.GONE

        binding.btnVioled.visibility = View.GONE
        binding.txtVioled.visibility = View.GONE

        binding.btnDefault.visibility = View.VISIBLE
        binding.txtDefault.visibility = View.VISIBLE

        for(i in 1..4){
            updateStateSensor(i, CMND_SENSOR_DEFAULT)
        }
    }

     fun configAlphaAnimation(violed: Boolean): AlphaAnimation {
         val alphaAnimation: AlphaAnimation = if (violed){
             AlphaAnimation(1.0f, 0.0f)
         } else{
             AlphaAnimation(1.0f, 0.2f)
         }
        alphaAnimation.duration = 500
        alphaAnimation.interpolator = LinearInterpolator()
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE
        return alphaAnimation
    }

    private fun stateSensor(imageView: ImageView, status: String) {
        when (status) {
            CMND_MQTT_SENSOR_CLOSE -> imageView.setBackgroundResource(R.drawable.img_sensor_default)
            CMND_MQTT_SENSOR_BYPASS -> imageView.setBackgroundResource(R.drawable.img_sensor_bypass)
            CMND_MQTT_SENSOR_OPEN -> imageView.setBackgroundResource(R.drawable.img_sensor_open)

            CMND_API_SENSOR_BYPASS -> imageView.setBackgroundResource(R.drawable.img_sensor_bypass)
            CMND_API_SENSOR_CLOSE -> imageView.setBackgroundResource(R.drawable.img_sensor_default)
            CMND_API_SENSOR_OPEN -> imageView.setBackgroundResource(R.drawable.img_sensor_open)

            CMND_SENSOR_DEFAULT -> imageView.setBackgroundResource(R.drawable.img_sensor_default)
        }
    }

    fun updateAllStateAlarm(list: List<Status>?) {
        when (list!![0].status) {
            CMND_API_VIOLED -> stateVioled()
            CMND_API_DISARM -> stateDisarm()
            CMND_API_ARM -> stateArm()
        }
        for (i in 1..5) {
            when (i) {
                1 -> stateSensor(binding.setor1ImgStatus, list[i].status)
                2 -> stateSensor(binding.setor2ImgStatus, list[i].status)
                3 -> stateSensor(binding.setor3ImgStatus, list[i].status)
                4 -> stateSensor(binding.setor4ImgStatus, list[i].status)
            }
        }
    }

    fun updateStateSensor(setor: Int, status: String) {
        Log.i(TAG_MQTT, "update sensor $setor for status $status")
        when (setor) {
            1 -> stateSensor(binding.setor1ImgStatus, status)
            2 -> stateSensor(binding.setor2ImgStatus, status)
            3 -> stateSensor(binding.setor3ImgStatus, status)
            4 -> stateSensor(binding.setor4ImgStatus, status)
        }
    }

    fun updateStateAlarm(status: String) {
        Log.d(TAG_MQTT, "update alarm for status $status")
        when (status) {
            CMND_API_VIOLED -> stateVioled()
            CMND_API_DISARM -> stateDisarm()
            CMND_API_ARM -> stateArm()
        }
    }

    fun showAlert(title: String, message: String){
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }
}