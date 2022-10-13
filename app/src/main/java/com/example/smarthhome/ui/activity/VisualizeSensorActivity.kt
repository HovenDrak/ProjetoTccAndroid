package com.example.smarthhome.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.databinding.ActivityBypassSensorsBinding
import org.koin.java.KoinJavaComponent

class VisualizeSensorActivity : AppCompatActivity() {

    private val db: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)

    private val binding by lazy {
        ActivityBypassSensorsBinding.inflate(layoutInflater)
    }

    private var sensor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadIntent()

        if (sensor != 0){
            val sensor = db.statusDAO.getStatusAlarm(sensor)

            when(sensor.status){
                "" -> {}

            }

        }


    }


    private fun loadIntent() {
        val intent = this.intent

        if (intent != null) {
            sensor = intent.getIntExtra("SENSOR", 0)
        }
    }
}