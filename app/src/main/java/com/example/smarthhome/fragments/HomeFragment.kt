package com.example.smarthhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.model.Status
import com.example.smarthhome.repository.AlarmRepository
import com.example.smarthhome.retrofit.ServiceBuilderApi
import com.example.smarthhome.service.Alarm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var show = false
    private val alarmCmnd: Alarm = Alarm()
    private val alarmeRepository = AlarmRepository()

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        alarmeRepository.getCurrentState(binding, requireContext())
        configButtonDesarm()
        configButtonArm()
        configButtonActiveDesarm()
        configButtonActiveArm()
        return binding.root
    }

    private fun configButtonActiveDesarm() {
        binding.btnActiveDesarm.setOnClickListener(View.OnClickListener {
            runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
            show = false
            Timer().schedule(700) {}
            alarmCmnd.stateDesarm(binding)
        })
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener(View.OnClickListener {
            runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
            show = false
            Timer().schedule(700) {}
            alarmCmnd.stateArm(binding)
        })
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener(View.OnClickListener {
            show = if (!show) {
                runAnimation(R.anim.animation_down, R.anim.animation_move_down, View.VISIBLE)
                true
            } else {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                false
            }
        })
    }

    private fun configButtonDesarm() {
        binding.btnDesarm.setOnClickListener(View.OnClickListener {

            show = if (!show) {
                runAnimation(R.anim.animation_down, R.anim.animation_move_down, View.VISIBLE)
                true
            } else {
                runAnimation(R.anim.animation_up, R.anim.animation_move_up, View.GONE)
                false
            }
        })
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