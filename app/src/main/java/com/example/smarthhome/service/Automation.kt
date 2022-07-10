package com.example.smarthhome.service

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentAutomationBinding
import com.example.smarthhome.model.Status
import com.example.smarthhome.constants.Constants.CMND_LIGHT_OFF
import com.example.smarthhome.constants.Constants.CMND_LIGHT_OFFLINE
import com.example.smarthhome.constants.Constants.CMND_LIGHT_ON
import com.example.smarthhome.constants.Constants.CMND_MQTT_LIGHT_OFF
import com.example.smarthhome.constants.Constants.CMND_MQTT_LIGHT_ON
import com.example.smarthhome.constants.Constants.BACKGROUND_COLOR_LIGHT_OFFLINE
import com.example.smarthhome.constants.Constants.BACKGROUND_COLOR_LIGHT_OFF
import com.example.smarthhome.constants.Constants.BACKGROUND_COLOR_LIGHT_ON
import com.example.smarthhome.constants.Constants.TEXT_LIGHT_OFFLINE
import com.example.smarthhome.constants.Constants.TEXT_LIGHT_OFF
import com.example.smarthhome.constants.Constants.TEXT_LIGHT_ON

class Automation {

    private lateinit var binding: FragmentAutomationBinding

    fun setBinding(binding: FragmentAutomationBinding){
        this.binding = binding
    }

    fun disableDefaultView(){
        binding.progressBarAutomation.visibility = View.GONE
    }

    private fun updateStateLight(cardView: CardView, textView: TextView, imageButton: ImageButton, state: String){
        when(state){
            CMND_LIGHT_OFFLINE -> lightOffline(cardView, textView, imageButton)
            CMND_MQTT_LIGHT_OFF -> lightOff(cardView, textView, imageButton)
            CMND_MQTT_LIGHT_ON -> lightOn(cardView, textView, imageButton)
            CMND_LIGHT_OFF -> lightOff(cardView, textView, imageButton)
            CMND_LIGHT_ON -> lightOn(cardView, textView, imageButton)
        }
    }

    private fun lightOn(cardView: CardView, textView: TextView, imageButton: ImageButton){
        cardView.setCardBackgroundColor(Color.parseColor(BACKGROUND_COLOR_LIGHT_ON))
        imageButton.setBackgroundResource(R.drawable.light_on)
        imageButton.startAnimation(AlphaAnimation(0.0f, 0.0f))
        textView.text = TEXT_LIGHT_ON
    }

    private fun lightOff(cardView: CardView, textView: TextView, imageButton: ImageButton){
        cardView.setCardBackgroundColor(Color.parseColor(BACKGROUND_COLOR_LIGHT_OFF))
        imageButton.setBackgroundResource(R.drawable.light_off)
        imageButton.startAnimation(AlphaAnimation(0.0f, 0.0f))
        textView.text = TEXT_LIGHT_OFF
    }

    private fun lightOffline(cardView: CardView, textView: TextView, imageButton: ImageButton){
        cardView.setCardBackgroundColor(Color.parseColor(BACKGROUND_COLOR_LIGHT_OFFLINE))
        imageButton.setBackgroundResource(R.drawable.light_off)
        imageButton.startAnimation(AlphaAnimation(0.0f, 0.0f))
        textView.text = TEXT_LIGHT_OFFLINE
    }

    fun updateAllStateAutomation(list: List<Status>?){
        for (i in 1..4) {
            Log.i("Automation", "update Light: $i Status: ${list!![i].status}")
            updateStateLight(i, list[i].status)
        }
    }

    fun updateStateLight(light: Int, state: String){
        when(light){
            1 -> updateStateLight(binding.cardMaterialLamp1, binding.txtStateLamp1, binding.btnLight1, state)
            2 -> updateStateLight(binding.cardMaterialLamp2, binding.txtStateLamp2, binding.btnLight2, state)
            3 -> updateStateLight(binding.cardMaterialLamp3, binding.txtStateLamp3, binding.btnLight3, state)
            4 -> updateStateLight(binding.cardMaterialLamp4, binding.txtStateLamp4, binding.btnLight4, state)
        }
    }

    fun configAlphaAnimation(): AlphaAnimation {
        val alphaAnimation = AlphaAnimation(1.0f, 0.2f)
        alphaAnimation.duration = 500
        alphaAnimation.interpolator = LinearInterpolator()
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE
        return alphaAnimation
    }
}