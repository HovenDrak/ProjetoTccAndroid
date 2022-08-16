package com.example.smarthhome.ui.animation

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ImageButton
import com.example.smarthhome.R
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.service.Alarm
import org.koin.java.KoinJavaComponent.inject

class Animations {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var context: Context

    private val alarmCmnd: Alarm by inject(Alarm::class.java)

    fun setConfig(binding: FragmentHomeBinding, context: Context){
        this.binding = binding
        this.context = context
    }

    fun animationShowCmndButtons(show: Boolean) {
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if(show){
                    binding.constraintCommand.layoutParams.height = (390 * interpolatedTime).toInt()
                } else{
                    binding.constraintCommand.layoutParams.height = (390 - (interpolatedTime * 370)).toInt()
                }
                binding.constraintCommand.requestLayout()
            }
        }
        animation.duration = 500
        binding.constraintCommand.startAnimation(animation)
    }

    fun animationSendCmnd(cmndArm: Boolean){
        if(cmndArm){
            binding.btnDisarm.startAnimation(alarmCmnd.configAlphaAnimation(false))
        } else{
            if(binding.btnVioled.visibility == View.GONE) {
                binding.btnArm.startAnimation(alarmCmnd.configAlphaAnimation(false))
            }
        }
    }

    fun animationBounce(imageButton: ImageButton, violed: Boolean){

        val animationBounce: Animation = AnimationUtils.loadAnimation(this.context, R.anim.bounce)
        animationBounce.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                imageButton.startAnimation(animationBounce)
            }

            override fun onAnimationEnd(p0: Animation?) {
                if(violed){
                    imageButton.startAnimation(alarmCmnd.configAlphaAnimation(violed))
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }
        })
        imageButton.startAnimation(animationBounce)
    }
}