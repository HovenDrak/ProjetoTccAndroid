package com.example.smarthhome.ui.recyclerview.adapter

import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthhome.databinding.ItemHistoryEventBinding
import com.example.smarthhome.model.Event
import java.sql.Time

class ListEventAdapter(
    private val context: Context,
    events: List<Event>
): RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    private val events = events.toMutableList()

    class ViewHolder(private val binding: ItemHistoryEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun insertData(event: Event){
            binding.txtEvent.text = "${event.descricao} por ${event.user}"
            val timeString = event.date.substring(event.date.indexOf(" ") + 1)
            val time = Time(timeString.substring(0, 2).toInt(),
                            timeString.substring(3, 5).toInt(),
                            timeString.substring(6).toInt())
            time.hours -= 3
            binding.txtEventDate.text = time.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryEventBinding
            .inflate(LayoutInflater
                .from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.insertData(events[position])
    }

    override fun getItemCount() = events.size

    fun refresh(events: List<Event>) {
        this.events.clear()
        this.events.addAll(events)
        notifyDataSetChanged()
    }
}