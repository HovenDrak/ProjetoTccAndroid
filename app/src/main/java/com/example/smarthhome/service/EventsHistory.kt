package com.example.smarthhome.service

import android.content.Context
import android.view.View
import com.example.smarthhome.databinding.FragmentEventsHistoryBinding
import com.example.smarthhome.model.Event
import com.example.smarthhome.ui.recyclerview.adapter.ListEventAdapter

class EventsHistory {

    private lateinit var binding: FragmentEventsHistoryBinding
    private lateinit var context: Context
    private lateinit var adapter: ListEventAdapter

    fun setBinding(binding: FragmentEventsHistoryBinding, context: Context){
        this.binding = binding
        this.context = context
    }

    fun configDateEvent(event: String, isEmpty: Boolean) {
        if(isEmpty){
            binding.txtDayEvent.text = "Hoje $event"
            return
        }
        binding.txtDayEvent.text = "Hoje ${event.substring(0, event.indexOf(" "))}"
    }

    fun configRecyclerView(listEvents: List<Event>){
        adapter = ListEventAdapter(context, listEvents)
        binding.recyclerViewEvents.adapter = adapter
    }

    fun refreshRecyclerView(listEvents: List<Event>){
        if(this::adapter.isInitialized){
            adapter.refresh(listEvents)
        }
    }

    fun activeNotFoundEvents(){
        binding.progressBarEventsHistory.visibility = View.GONE
        binding.txtNotFoundEvents.visibility = View.VISIBLE
    }

    fun activeWidgetsView(){
        binding.progressBarEventsHistory.visibility = View.GONE
        binding.txtNotFoundEvents.visibility = View.GONE
        binding.txtDayEvent.visibility = View.VISIBLE
        binding.recyclerViewEvents.visibility = View.VISIBLE
    }
}