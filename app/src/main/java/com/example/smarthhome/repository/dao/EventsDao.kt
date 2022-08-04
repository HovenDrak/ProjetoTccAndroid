package com.example.smarthhome.repository.dao

import com.example.smarthhome.model.Event

class EventsDao {

    companion object {
        private val events = mutableListOf<Event>()
    }

    private fun addEvent(event: Event) {
        events.add(event)
    }

    fun getAllEvents(): List<Event> {
        return events.toList()
    }

    fun getEvent(position: Int): Event{
        return events[position]
    }

    fun clearAndAddList(listEvent: List<Event>){
        events.clear()
        for(e in listEvent){
            addEvent(e)
        }
    }
}