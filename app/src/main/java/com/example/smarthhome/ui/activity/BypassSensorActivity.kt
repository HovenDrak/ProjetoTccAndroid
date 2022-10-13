package com.example.smarthhome.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthhome.R
import com.example.smarthhome.database.AppDatabase
import com.example.smarthhome.database.models.AlarmDB
import com.example.smarthhome.databinding.ActivityBypassSensorsBinding
import com.example.smarthhome.ui.`interface`.OnItemClick
import com.example.smarthhome.ui.adapter.SensorBypassAdapter
import com.example.smarthhome.ui.fragment.HomeFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.java.KoinJavaComponent

class BypassSensorActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityBypassSensorsBinding.inflate(layoutInflater)
    }

    private val db: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)
    private var sensorDetails: ArrayList<AlarmDB> = arrayListOf()
    private var sensorBypass: ArrayList<Int> = arrayListOf(0, 0, 0, 0)
    private var sensors: ArrayList<Int> = arrayListOf()

    private lateinit var adapter: SensorBypassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadIntent()
        setAllBypassDB(0, listOf(1, 2, 3, 4), false)

        sensors.forEach {
            sensorDetails.add(db.statusDAO.getStatusAlarm(it))
        }
        configAdapter()

        binding.constraintLayoutBypassSensor.setOnClickListener {
            this.finish()
        }

        binding.btnByspassSensors.setOnClickListener {
            val list: ArrayList<Int> = arrayListOf()

            for (i in 0 until sensorBypass.size){
                if(sensorBypass[i] != 0){
                    list.add(i + 1)
                }
            }
            Log.i("list", list.toString())
            setAllBypassDB(1, list, true)
        }
    }

    private fun loadIntent() {
        val intent = this.intent

        if (intent != null) {
            sensors = intent.getIntegerArrayListExtra("SENSORS")!!
        }
    }

    private fun configAdapter() {
        adapter = SensorBypassAdapter(this, sensorDetails,
            onItemClick = object : OnItemClick {
                override fun onClick(view: View, position: Int) {
                    val switch = view.findViewById<SwitchMaterial>(R.id.switchSensorBypass)
                    val text = view.findViewById<TextView>(R.id.txtNameSensor).text
                    val pos = text.substring(0, 1).toInt() - 1
                    sensorBypass[pos] = if (switch.isChecked) 1 else 0
                    Log.i("sensorBypass", sensorBypass.toString())
                }
            })
        binding.recyclerViewSensors.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSensors.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setAllBypassDB(bypass: Int, listSensors: List<Int>, finish: Boolean){
        listSensors.forEach {
            db.statusDAO.updateStateBypass(bypass, it)
        }
        if(finish){
            setResult(RESULT_OK)
            this.finish()
        }
    }
}