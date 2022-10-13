package com.example.smarthhome.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.smarthhome.constants.Constants.LIST_TOPIC_ALARM
import com.example.smarthhome.constants.Constants.SEND_CMND_ARM
import com.example.smarthhome.constants.Constants.SEND_CMND_DISARM
import com.example.smarthhome.constants.Constants.TOPIC_CMND_ALARM
import com.example.smarthhome.constants.Constants.TOPIC_CMND_ALARM_BYPASS
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.databinding.FragmentHomeBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.service.Alarm
import com.example.smarthhome.ui.`interface`.ResultVerifySensor
import com.example.smarthhome.ui.activity.BypassSensorActivity
import com.example.smarthhome.ui.animation.Animations
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent


class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mqttClient: MqttAndroidClient by inject()
    private val anim: Animations by inject()
    private val alarmCmnd: Alarm by inject()

    private val mqttRepository = MqttRepository(mqttClient)
    private val apiRepository = ApiRepository()
    private var show = false

    private val db: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK){
                cmndBypassSensors()
                show = false
                anim.animationSendCmnd(true)
                Toast.makeText(context, "Comando de Bypass enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
        alarmCmnd.setConfigAlarm(binding, context!!)
        anim.setConfig(binding, context!!)
        mqttConfig()
        configButtons()
        apiRepository.getCurrentStateHome(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttRepository.unsubscribeTopics(LIST_TOPIC_ALARM)
        _binding = null
        show = false
    }

    override fun onResume() {
        super.onResume()
        show = false
    }

    private fun mqttConfig() {
        mqttRepository.setFragmentCallback(0)
        mqttRepository.subscribeTopics(LIST_TOPIC_ALARM)
    }

    private fun configButtons() {
        configButtonActiveDisarm()
        configButtonActiveArm()
        configButtonVioled()
        configButtonDisarm()
        configButtonArm()
    }

    private fun configButtonActiveDisarm() {
        binding.btnActiveDesarm.setOnClickListener{
            if(show) {
                anim.animationShowCmndButtons(false)
                anim.animationBounce(binding.btnActiveDesarm, false)
                sendCommand(SEND_CMND_DISARM)
                anim.animationSendCmnd(false)
                show = false
                Toast.makeText(context, "Comando de Desarme enviado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configButtonActiveArm() {
        binding.btnActiveArm.setOnClickListener{
            if(show) {
                apiRepository.getCurrentStateHome(context!!)
                anim.animationShowCmndButtons(false)
                anim.animationBounce(binding.btnActiveArm, false)

                apiRepository.verifySensorArm(requireContext(), callback = object :
                    ResultVerifySensor {
                    override fun finish(ok: Boolean, list: ArrayList<Int>) {
                        if (ok){
                            if (list.isEmpty()){
                                sendCommand(SEND_CMND_ARM)
                                anim.animationSendCmnd(true)
                                show = false
                                Toast.makeText(context, "Comando de Arme enviado com sucesso.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val intent = Intent(requireContext(), BypassSensorActivity::class.java)
                                intent.putIntegerArrayListExtra("SENSORS", list)
                                startForResult.launch(intent)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun configButtonVioled(){
        binding.btnVioled.setOnClickListener{
            anim.animationBounce(binding.btnVioled, true)
            show = configAnimation()
        }
    }

    private fun configButtonArm() {
        binding.btnArm.setOnClickListener{
            anim.animationBounce(binding.btnArm, false)
            show = configAnimation()
        }
    }

    private fun configButtonDisarm() {
        binding.btnDisarm.setOnClickListener{
            anim.animationBounce(binding.btnDisarm, false)
            show = configAnimation()
        }
    }

    private fun configAnimation() =
        if (!show) {
            anim.animationShowCmndButtons(true)
            true
        } else {
            anim.animationShowCmndButtons(false)
            false
        }

    private fun sendCommand(cmnd: String){
        mqttRepository.publish(TOPIC_CMND_ALARM, cmnd)
    }

    private fun cmndBypassSensors() {
        val sensorsBypass = db.statusDAO.getAllSensorsBypass()
        var cmnd = "{\"setor_bypass\": ["

        if (sensorsBypass.size == 1) {
            cmnd += "{\"setor\": ${sensorsBypass[0] - 1}}"
        } else if (sensorsBypass.size > 1) {
            for (i in 0..sensorsBypass.size - 2) {
                cmnd += "{\"setor\": ${sensorsBypass[i] - 1}},"
            }
            cmnd += "{\"setor\": ${sensorsBypass[sensorsBypass.size - 1] - 1}}"
        }
        cmnd += "]}"
        mqttRepository.publish("cmnd/alarme/bypass", cmnd)
    }
}