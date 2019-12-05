package com.bogiruapps.rdshapp.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R

class EventsAdapter(val events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_item, parent, false)
        return EventsHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventsHolder, position: Int) {

    }


    class EventsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}