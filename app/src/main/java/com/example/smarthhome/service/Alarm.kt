package com.example.smarthhome.service

import android.content.Context
import com.example.smarthhome.databinding.FragmentHomeBinding
import android.view.animation.LinearInterpolator
import android.view.animation.AlphaAnimation
import com.example.smarthhome.model.Status
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_CLOSE
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_OPEN
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_CLOSE
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_OPEN
import com.example.smarthhome.constants.Constants.CMND_SENSOR_DEFAULT
import com.example.smarthhome.constants.Constants.CMND_API_ARM
import com.example.smarthhome.constants.Constants.CMND_API_DISARM
import com.example.smarthhome.constants.Constants.CMND_API_VIOLED
import android.view.animation.Animation
import com.example.smarthhome.R
import android.widget.ImageView
import android.view.View
import com.example.smarthhome.constants.Constants.CMND_API_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.CMND_MQTT_ARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_DISARM
import com.example.smarthhome.constants.Constants.CMND_MQTT_SENSOR_BYPASS
import com.example.smarthhome.constants.Constants.CMND_MQTT_VIOLED
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.database.AppDatabase
import org.koin.java.KoinJavaComponent

class Alarm{

    private val db: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)
    private lateinit var binding: FragmentHomeBinding
    private lateinit var context: Context
    private var canArm = false

    fun setConfigAlarm(binding: FragmentHomeBinding, context: Context){
        this.binding = binding
        this.context = context
    }

    private fun stateArm() {
        binding.materialCardDisarm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.materialCardDisarm.visibility = View.GONE

        binding.materialCardArm.visibility = View.VISIBLE
        binding.materialCardActiveArm.visibility = View.GONE
        binding.materialCardActiveDisarm.visibility = View.VISIBLE
    }

    private fun stateDisarm() {
        binding.materialCardArm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.materialCardArm.visibility = View.GONE

        binding.materialCardVioled.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.materialCardVioled.visibility = View.GONE

        binding.materialCardDisarm.visibility = View.VISIBLE
        binding.materialCardActiveArm.visibility = View.VISIBLE
        binding.materialCardActiveDisarm.visibility = View.GONE
    }

    private fun stateVioled() {
        binding.materialCardActiveArm.visibility = View.GONE
        binding.materialCardActiveDisarm.visibility = View.VISIBLE

        binding.materialCardArm.startAnimation(AlphaAnimation(0.0f, 0.0f))
        binding.materialCardArm.visibility = View.GONE

        binding.materialCardVioled.visibility = View.VISIBLE
        binding.btnVioled.startAnimation(configAlphaAnimation(true))
    }

     fun disableIconsDefault() {
        binding.progressBarAlarm.visibility = View.GONE
        binding.materialCardDefault.visibility = View.GONE
    }

     fun configAlphaAnimation(violed: Boolean): AlphaAnimation {
         val alphaAnimation: AlphaAnimation = if (violed) AlphaAnimation(1.0f, 0.0f) else AlphaAnimation(1.0f, 0.2f)
         alphaAnimation.interpolator = LinearInterpolator()
         alphaAnimation.repeatCount = Animation.INFINITE
         alphaAnimation.repeatMode = Animation.REVERSE
         alphaAnimation.duration = 500
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
        updateStateAlarm(list!![0].status)

        for (i in 1..5) {
            when (i) {
                1 -> stateSensor(binding.setor1ImgStatus, list[i].status)
                2 -> stateSensor(binding.setor2ImgStatus, list[i].status)
                3 -> stateSensor(binding.setor3ImgStatus, list[i].status)
            }
        }
    }

    fun updateDeviceAlarm(device: String, status: String) {
        when (device) {
            LIST_TOPIC_ALARM[0] -> updateStateAlarm(status)
            LIST_TOPIC_ALARM[1] -> {
                stateSensor(binding.setor1ImgStatus, status)
                db.statusDAO.updateStatusAlarm(status, if (status == CMND_API_SENSOR_BYPASS) 1 else 0,1)
            }
            LIST_TOPIC_ALARM[2] -> {
                stateSensor(binding.setor2ImgStatus, status)
                db.statusDAO.updateStatusAlarm(status, if (status == CMND_API_SENSOR_BYPASS) 1 else 0,2)
            }
            LIST_TOPIC_ALARM[3] -> {
                stateSensor(binding.setor3ImgStatus, status)
                db.statusDAO.updateStatusAlarm(status, if (status == CMND_API_SENSOR_BYPASS) 1 else 0,3)
            }
        }
    }

    private fun updateStateAlarm(status: String) {
        when (status) {
            CMND_API_VIOLED, CMND_MQTT_VIOLED -> stateVioled()
            CMND_API_DISARM, CMND_MQTT_DISARM -> stateDisarm()
            CMND_API_ARM, CMND_MQTT_ARM -> stateArm()
        }
    }
}