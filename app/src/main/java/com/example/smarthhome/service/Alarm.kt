package com.example.smarthhome.service

import android.view.View
import android.widget.ImageView
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.model.Status

class Alarm(){

     fun stateArm(binding: FragmentHomeBinding){
        binding.btnDesarm.visibility = View.GONE
        binding.txtDesarm.visibility = View.GONE
        binding.btnArm.visibility = View.VISIBLE
        binding.txtArm.visibility = View.VISIBLE

        binding.btnActiveArm.visibility = View.GONE
        binding.txtActiveArm.visibility = View.GONE
        binding.btnActiveDesarm.visibility = View.VISIBLE
        binding.txtActiveDesarm.visibility = View.VISIBLE
    }

     fun stateDesarm(binding: FragmentHomeBinding){
        binding.btnDesarm.visibility = View.VISIBLE
        binding.txtDesarm.visibility = View.VISIBLE
        binding.btnArm.visibility = View.GONE
        binding.txtArm.visibility = View.GONE

        binding.btnActiveArm.visibility = View.VISIBLE
        binding.txtActiveArm.visibility = View.VISIBLE
        binding.btnActiveDesarm.visibility = View.GONE
        binding.txtActiveDesarm.visibility = View.GONE
    }

    fun stateSensor(imageView: ImageView, status: String){
        when(status){
            "fechado" -> imageView.setBackgroundResource(R.mipmap.img_setor_close)
            "aberto" -> imageView.setBackgroundResource(R.mipmap.img_setor_open)
        }

    }

    fun updateState(binding: FragmentHomeBinding, list: List<Status>?){
        when(list!![0].status){
            "desarmado" -> stateDesarm(binding)
            "armado" -> stateArm(binding)
        }
        for(i in 1..5){
            when(i){
                1 -> stateSensor(binding.setor1ImgStatus, list[i].status)
                2 -> stateSensor(binding.setor2ImgStatus, list[i].status)
                3 -> stateSensor(binding.setor3ImgStatus, list[i].status)
                4 -> stateSensor(binding.setor4ImgStatus, list[i].status)
            }
        }

    }
}