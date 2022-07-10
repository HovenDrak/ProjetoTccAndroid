package com.example.smarthhome.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.constants.Constants.CMND_LIGHT_OFF
import com.example.smarthhome.constants.Constants.CMND_LIGHT_ON
import com.example.smarthhome.constants.Constants.LIST_TOPIC_LIGHT
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.databinding.FragmentAutomationBinding
import com.example.smarthhome.model.StatusDB
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Automation
import com.google.android.material.card.MaterialCardView
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject

class AutomationFragment : Fragment() {

    private var _binding: FragmentAutomationBinding? = null
    private val binding get() = _binding!!

    private val mqttClient: MqttAndroidClient by inject()
    private val automationCmnd: Automation by inject()
    private val db: AppDatabase by inject()

    private val mqttRepository = MqttRepository(mqttClient)
    private val apiRepository = ApiRepository()

    private var listStatus: List<StatusDB> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutomationBinding.inflate(inflater, container, false)
        automationCmnd.setBinding(binding)
        apiRepository.getCurrentStateAutomation(requireContext())
        mqttConfig()
        configButtons()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttRepository.unsubscribeTopics(LIST_TOPIC_LIGHT)
        _binding = null
    }

    private fun mqttConfig() {
        mqttRepository.setFragmentCallback(1)
        mqttRepository.subscribeTopics(LIST_TOPIC_LIGHT)
    }

    private fun configButtons() {
        binding.btnLight1.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp1)
            sendCmnd(1, LIST_TOPIC_LIGHT[0])
        }

        binding.btnLight2.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp2)
            sendCmnd(2, LIST_TOPIC_LIGHT[1])
        }

        binding.btnLight3.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp3)
            sendCmnd(3, LIST_TOPIC_LIGHT[2])
        }

        binding.btnLight4.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp4)
            sendCmnd(4, LIST_TOPIC_LIGHT[3])
        }
    }

    private fun animationBounce(materialCardView: MaterialCardView){
        materialCardView.startAnimation(AnimationUtils
            .loadAnimation(this.context, R.anim.bounce))
    }

    private fun sendCmnd(light: Int, topicLight: String){
        if(listStatus[light].status == CMND_LIGHT_ON){
            mqttRepository.publish("cmnd/$topicLight", CMND_LIGHT_OFF)
            Toast.makeText(context, "Comando enviado com sucesso.", Toast.LENGTH_SHORT).show()
        } else if(listStatus[light].status == CMND_LIGHT_OFF){
            mqttRepository.publish("cmnd/$topicLight", CMND_LIGHT_ON)
            Toast.makeText(context, "Comando enviado com sucesso.", Toast.LENGTH_SHORT).show()
        }
        animationSendCmnd(light)
    }
    
    private fun animationSendCmnd(light: Int){
        when(light){
            1 -> binding.btnLight1.startAnimation(automationCmnd.configAlphaAnimation())
            2 -> binding.btnLight2.startAnimation(automationCmnd.configAlphaAnimation())
            3 -> binding.btnLight3.startAnimation(automationCmnd.configAlphaAnimation())
            4 -> binding.btnLight4.startAnimation(automationCmnd.configAlphaAnimation())
        }
    }
}