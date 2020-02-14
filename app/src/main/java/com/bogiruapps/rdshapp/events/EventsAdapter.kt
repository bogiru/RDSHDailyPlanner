package com.bogiruapps.rdshapp.events

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.EventsItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventsAdapter(
    options : FirestoreRecyclerOptions<SchoolEvent>,
    private val viewModel: EventViewModel
) :
    FirestoreRecyclerAdapter<SchoolEvent, EventsAdapter.EventsViewHolder>(options) {

    override fun onBindViewHolder(p0: EventsViewHolder, p1: Int, p2: SchoolEvent) {
        p2.id = snapshots.getSnapshot(p1).reference.id
        p0.bind(viewModel, p2)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder.from(parent)
    }

    class EventsViewHolder(private val binding: EventsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: EventViewModel, schoolEvent: SchoolEvent) {
            binding.event = schoolEvent
            binding.viewModel = viewModel
            if (schoolEvent.amountTask == 0) binding.progressBar2.progress = 0
            else binding.progressBar2.progress = schoolEvent.amountCompletedTask * 100 / schoolEvent.amountTask
            binding.executePendingBindings()

        }



        companion object {
            fun from(parent: ViewGroup): EventsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EventsItemBinding.inflate(layoutInflater, parent, false)
                return EventsViewHolder(binding)
            }
        }
    }
}