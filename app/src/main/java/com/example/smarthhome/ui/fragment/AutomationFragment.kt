package com.example.smarthhome.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.constants.Constants.CMND_API_GARAGE_CLOSE
import com.example.smarthhome.constants.Constants.CMND_API_GARAGE_OPEN
import com.example.smarthhome.constants.Constants.CMND_LIGHT_OFF
import com.example.smarthhome.constants.Constants.CMND_LIGHT_ON
import com.example.smarthhome.constants.Constants.CMND_MQTT_GARAGE_CLOSE
import com.example.smarthhome.constants.Constants.CMND_MQTT_GARAGE_OPEN
import com.example.smarthhome.constants.Constants.LIST_TOPIC_AUTOMATION
import com.example.smarthhome.constants.Constants.TOPIC_LIGHT
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.database.models.AutomationDB
import com.example.smarthhome.databinding.FragmentAutomationBinding
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

    private var listStatus: List<AutomationDB> = listOf()
    private var showIlumination = true
    private var showGarage = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAutomationBinding.inflate(inflater, container, false)
        automationCmnd.setBinding(binding)
        apiRepository.getCurrentStateAutomation(requireContext())
        configButtons()

        binding.materialCardIlumination.setOnClickListener {
            binding.lineSeparatorLighting.visibility = if(showIlumination) View.VISIBLE else View.GONE
            binding.scrollViewILighting.visibility = if(showIlumination) View.VISIBLE else View.GONE
            binding.arrowUpDownIlumination.setImageDrawable(
                if(showIlumination) requireContext().getDrawable(R.drawable.ic_arrow_up)
                else requireContext().getDrawable(R.drawable.ic_arrow_down))
            showIlumination = !showIlumination
        }

        binding.materialCardGarage.setOnClickListener {
            binding.constraintLayoutGarage.visibility = if(showGarage) View.VISIBLE else View.GONE
            binding.lineSeparatorGarage.visibility = if(showGarage) View.VISIBLE else View.GONE
            binding.arrowUpDownGarage.setImageDrawable(
                if(showGarage) requireContext().getDrawable(R.drawable.ic_arrow_up)
                else requireContext().getDrawable(R.drawable.ic_arrow_down))
            showGarage = !showGarage
        }
        return binding.root
    }

    override fun onDestroyView() {
        showIlumination = true
        showGarage = true
        _binding = null
        super.onDestroyView()
    }

    private fun configButtons() {
        binding.btnLight1.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp1)
            sendCmndLight(1)
        }

        binding.btnLight2.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp2)
            sendCmndLight(2)
        }

        binding.btnLight3.setOnClickListener {
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialLamp3)
            sendCmndLight(3)
        }

        binding.btnGarage.setOnClickListener{
            listStatus = db.statusDAO.consultAllState()
            animationBounce(binding.cardMaterialGarage)
            sendCmndGarage(LIST_TOPIC_AUTOMATION[4])
        }
    }

    private fun animationBounce(materialCardView: MaterialCardView){
        materialCardView.startAnimation(AnimationUtils
            .loadAnimation(this.context, R.anim.bounce))
    }

    private fun sendCmndLight(light: Int){
        if (listStatus[light].status == CMND_LIGHT_ON)
            cmdLight(light, CMND_LIGHT_OFF)

        else if (listStatus[light].status == CMND_LIGHT_OFF)
            cmdLight(light, CMND_LIGHT_ON)

        animationSendCmnd(light)
    }

    private fun sendCmndGarage(topicGarage: String){
        if (!mqttClient.isConnected)
            mqttRepository.connectMqtt()

        if (listStatus[0].status == CMND_API_GARAGE_OPEN){
            mqttRepository.publish("cmnd/$topicGarage", "{\"newState\": $CMND_MQTT_GARAGE_CLOSE, \"user\": \"Mobile\"}")
            Toast.makeText(context, "Comando enviado com sucesso.", Toast.LENGTH_SHORT).show()

        } else if(listStatus[0].status == CMND_API_GARAGE_CLOSE){
            mqttRepository.publish("cmnd/$topicGarage", "{\"newState\": $CMND_MQTT_GARAGE_OPEN, \"user\": \"Mobile\"}")
            Toast.makeText(context, "Comando enviado com sucesso.", Toast.LENGTH_SHORT).show()
        }
        animationSendCmnd(5)
    }
    
    private fun animationSendCmnd(device: Int){
        when(device){
            1 -> binding.btnLight1.startAnimation(automationCmnd.configAlphaAnimation())
            2 -> binding.btnLight2.startAnimation(automationCmnd.configAlphaAnimation())
            3 -> binding.btnLight3.startAnimation(automationCmnd.configAlphaAnimation())
            5 -> binding.btnGarage.startAnimation(automationCmnd.configAlphaAnimation())
        }
    }

    private fun cmdLight(light: Int, cmd: String){
        if (mqttClient.isConnected) {
            mqttRepository.publish("cmnd/$TOPIC_LIGHT",
                "{\"light\": ${light - 1}, \"newState\": \"$cmd\", \"user\": \"Mobile\"}")
            Toast.makeText(context, "Comando enviado com sucesso.", Toast.LENGTH_SHORT).show()
        } else
            mqttRepository.connectMqtt(
                "cmnd/$TOPIC_LIGHT",
                "{\"light\": ${light - 1}, \"newState\": \"$cmd\", \"user\": \"Mobile\"}")
    }
}