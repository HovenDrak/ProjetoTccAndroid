package com.example.smarthhome.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthhome.R
import com.example.smarthhome.databinding.ItemHistoryEventBinding
import com.example.smarthhome.model.Event
import com.example.smarthhome.constants.Constants.NAME_SETOR_1
import com.example.smarthhome.constants.Constants.NAME_SETOR_2
import com.example.smarthhome.constants.Constants.NAME_SETOR_3
import com.example.smarthhome.constants.Constants.NAME_SETOR_4
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListEventAdapter(
    private val context: Context,
    events: List<Event>
): RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    private val events = events.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryEventBinding.inflate(LayoutInflater
                .from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.insertData(events[position])
    }

    override fun getItemCount() = events.size


    class ViewHolder(private val binding: ItemHistoryEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun insertData(event: Event){
            val pair = configTxtAndImgEvent(event)
            val time = LocalDateTime.parse(event.date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).minusHours(3)

            if(pair.first!=0)
                binding.imgEvent.setBackgroundResource(pair.first)

            binding.txtEvent.text = pair.second
            binding.txtEventDate.text = time.toLocalTime().toString()
        }

        private fun configTxtAndImgEvent(event: Event): Pair<Int, String> {
            var textEvent = ""
            var imgEvent = 0

            when (event.type) {
                "arm" -> {
                    textEvent = "${event.descricao} por ${event.user}"
                    imgEvent = R.drawable.ic_arm
                }
                "disarm" -> {
                    textEvent = "${event.descricao} por ${event.user}"
                    imgEvent = R.drawable.ic_disarm
                }
                "violed" -> {
                    textEvent = "${event.descricao} por ${event.user}"
                    imgEvent = R.drawable.ic_alarm_violed
                }
                "setor1Violed" -> {
                    textEvent = "Sensor $NAME_SETOR_1 em Disparo"
                    imgEvent = R.drawable.img_sensor_open
                }
                "setor2Violed" -> {
                    textEvent = "Sensor $NAME_SETOR_2 em Disparo"
                    imgEvent = R.drawable.img_sensor_open
                }
                "setor3Violed" -> {
                    textEvent = "Sensor $NAME_SETOR_3 em Disparo"
                    imgEvent = R.drawable.img_sensor_open
                }
                "setor4Violed" -> {
                    textEvent = "Sensor $NAME_SETOR_4 em Disparo"
                    imgEvent = R.drawable.img_sensor_open
                }
            }
            return Pair(imgEvent, textEvent)
        }
    }

    fun refresh(events: List<Event>) {
        this.events.clear()
        this.events.addAll(events)
        this.notifyDataSetChanged()
    }
}