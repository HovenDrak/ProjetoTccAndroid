package com.example.smarthhome.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthhome.database.models.AlarmDB
import com.example.smarthhome.databinding.ItemBypassSensorsBinding
import com.example.smarthhome.ui.`interface`.OnItemClick

class SensorBypassAdapter(
    private val context: Context,
    sensors: ArrayList<AlarmDB>,
    private val onItemClick: OnItemClick
): RecyclerView.Adapter<SensorBypassAdapter.ViewHolder>() {

    private val sensors = sensors.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBypassSensorsBinding.inflate(LayoutInflater
            .from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.insertData(sensors[position])

        holder.materialCard.setOnClickListener {
            holder.switch.isChecked = !holder.switch.isChecked
            onItemClick.onClick(holder.itemView, position)
        }
    }

    override fun getItemCount() = sensors.size


    class ViewHolder(private val binding: ItemBypassSensorsBinding) : RecyclerView.ViewHolder(binding.root) {

        val materialCard = binding.materialCardBypassSensor
        val switch = binding.switchSensorBypass

        fun insertData(sensor: AlarmDB){
            binding.txtNameSensor.text = sensor.showName
        }
    }

    fun refresh(sensors: List<AlarmDB>) {
        this.sensors.clear()
        this.sensors.addAll(sensors)
        this.notifyDataSetChanged()
    }
}