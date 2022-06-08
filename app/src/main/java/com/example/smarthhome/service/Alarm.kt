package com.example.smarthhome.service

import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.model.Status

class Alarm: Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    fun setBinding(binding: FragmentHomeBinding){
        this.binding = binding
    }

    private fun stateArm() {
        Log.d("MQTT", "atualizando status alarme para ARMADO")

        binding.btnDesarm.visibility = View.GONE
        binding.txtDesarm.visibility = View.GONE
        binding.btnArm.visibility = View.VISIBLE
        binding.txtArm.visibility = View.VISIBLE

        binding.btnActiveArm.visibility = View.GONE
        binding.txtActiveArm.visibility = View.GONE
        binding.btnActiveDesarm.visibility = View.VISIBLE
        binding.txtActiveDesarm.visibility = View.VISIBLE
    }

    private fun stateDisarm() {
        Log.d("MQTT", "atualizando status alarme para DESARMADO")

        binding.btnDesarm.visibility = View.VISIBLE
        binding.txtDesarm.visibility = View.VISIBLE
        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE

        binding.btnActiveArm.visibility = View.VISIBLE
        binding.txtActiveArm.visibility = View.VISIBLE
        binding.btnActiveDesarm.visibility = View.GONE
        binding.txtActiveDesarm.visibility = View.GONE
    }

    private fun stateVioled() {
        Log.d("MQTT", "atualizando status alarme para DISPARADO")

        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE
        binding.btnVioled.visibility = View.VISIBLE
        binding.txtVioled.visibility = View.VISIBLE

        binding.btnVioled.startAnimation(configAnimationVioled())
    }

     fun disableDefault() {
        binding.progressBarAlarm.visibility = View.GONE
        binding.btnDefault.visibility = View.GONE
        binding.txtDefault.visibility = View.GONE
    }

    private fun configAnimationVioled(): AlphaAnimation {
        val animationVioled = AlphaAnimation(1.0f, 0.0f)
        animationVioled.duration = 500
        animationVioled.interpolator = LinearInterpolator()
        animationVioled.repeatCount = Animation.INFINITE
        animationVioled.repeatMode = Animation.REVERSE
        return animationVioled
    }

    private fun stateSensor(imageView: ImageView, status: String) {
        Log.d("MQTT", "atualizando status para $status")
        when (status) {
            "fechado" -> imageView.setBackgroundResource(R.mipmap.img_setor_close)
            "\"fechado\"" -> imageView.setBackgroundResource(R.mipmap.img_setor_close)
            "aberto" -> imageView.setBackgroundResource(R.mipmap.img_setor_open)
            "\"aberto\"" -> imageView.setBackgroundResource(R.mipmap.img_setor_open)

        }
    }

    fun updateAllStateAlarm(list: List<Status>?) {
        when (list!![0].status) {
            "desarmado" -> stateDisarm()
            "armado" -> stateArm()
            "disparado" -> stateVioled()
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
        Log.i("MQTT", "atualizando status setor $setor")
        when (setor) {
            1 -> stateSensor(binding.setor1ImgStatus, status)
            2 -> stateSensor(binding.setor2ImgStatus, status)
            3 -> stateSensor(binding.setor3ImgStatus, status)
            4 -> stateSensor(binding.setor4ImgStatus, status)
        }

    }

    fun updateStateAlarme(status: String) {
        Log.d("MQTT", "atualizando status ALARME para $status")
        when (status) {
            "\"desarmado\"" -> stateDisarm()
            "\"armado\"" -> stateArm()
            "\"disparado\"" -> stateVioled()
        }
    }
}