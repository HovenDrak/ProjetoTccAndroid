package com.example.smarthhome.service

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentAutomationBinding
import com.example.smarthhome.model.Status

class Automation {

    private lateinit var binding: FragmentAutomationBinding

    fun setBinding(binding: FragmentAutomationBinding){
        this.binding = binding
    }

    fun disableDefaultView(){
        binding.progressBarAutomation.visibility = View.GONE
    }

    fun updateAllStateAutomation(list: List<Status>?){
        for (i in 1..4) {
            Log.i("Automation", "update Light: $i Status: ${list!![i].status}")
            updateStateLight(i, list[i].status)
        }
    }

    private fun updateStateLight(cardView: CardView, textView: TextView, imageButton: ImageButton, state: String){
        when(state){
            "Offline" -> {
                cardView.setCardBackgroundColor(Color.parseColor("#7B7B7B"))
                imageButton.setBackgroundResource(R.drawable.light_off)
                textView.text = "Offline"
            }
            "desligado" -> {
                cardView.setCardBackgroundColor(Color.parseColor("#DCDCDC"))
                imageButton.setBackgroundResource(R.drawable.light_off)
                textView.text = "Desligado"
            }
            "ligado" -> {
                cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                imageButton.setBackgroundResource(R.drawable.light_on)
                textView.text = "Ligado"
            }
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
}