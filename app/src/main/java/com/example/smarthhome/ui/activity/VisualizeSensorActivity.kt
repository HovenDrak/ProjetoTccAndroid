package com.example.smarthhome.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthhome.R
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.databinding.ActivityBypassSensorsBinding
import com.example.smarthhome.databinding.ActivityVizualizeSensorBinding
import com.example.smarthhome.repository.MqttRepository
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent

class VisualizeSensorActivity : AppCompatActivity() {

    private val db: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)
    private val mqttClient: MqttAndroidClient by inject()

    private val mqttRepository = MqttRepository(mqttClient)

    private val binding by lazy {
        ActivityVizualizeSensorBinding.inflate(layoutInflater)
    }
    private var sensor: Int = 0
    private var bypass: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadIntent()

        if (sensor != 0){
            val sensor = db.statusDAO.getStatusAlarm(sensor)

            binding.switchMaterial.isChecked = sensor.bypass == 1
            bypass = sensor.bypass == 1

            when(sensor.status){
                "fechado" -> {binding.imageView.setImageDrawable(getDrawable(R.drawable.img_sensor_default_full))}
                "bypassed" -> {binding.imageView.setImageDrawable(getDrawable(R.drawable.img_sensor_bypass))}
                "aberto" -> {binding.imageView.setImageDrawable(getDrawable(R.drawable.img_sensor_open))}
            }

            if (bypass)
                binding.materialCardView.isEnabled = false

            binding.edtNameSensor.setText(sensor.showName)
            binding.edtStatusSensor.setText(sensor.status)
            binding.switchMaterial.isChecked = sensor.bypass == 1
        }

        binding.btnSave.setOnClickListener {
            if(bypass){
                mqttRepository.publish("cmnd/alarme/bypass", "{\"setor_bypass\": [{\"setor\": $sensor}], \"user\": \"Mobile\"}")
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        binding.constraintLauyoutVisualize.setOnClickListener {
            finish()
        }

        binding.materialCardView.setOnClickListener {
            binding.switchMaterial.isChecked = !bypass
            bypass = !bypass
        }
    }


    private fun loadIntent() {
        val intent = this.intent

        if (intent != null) {
            sensor = intent.getIntExtra("SENSOR", 0)
        }
    }
}