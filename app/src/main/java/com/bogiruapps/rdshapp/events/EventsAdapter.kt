package com.bogiruapps.rdshapp.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.EventsItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventsAdapter(
    options : FirestoreRecyclerOptions<SchoolEvent>,
    private val viewModel: EventsViewModel
) :
    FirestoreRecyclerAdapter<SchoolEvent, EventsAdapter.EventsViewHolder>(options) {

    override fun onBindViewHolder(p0: EventsViewHolder, p1: Int, p2: SchoolEvent) {
        p0.bind(viewModel, p2)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder.from(parent)
    }

    class EventsViewHolder(private val binding: EventsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: EventsViewModel, schoolEvent: SchoolEvent) {
            binding.event = schoolEvent
            binding.viewModel = viewModel
            if (schoolEvent.countTask == 0) binding.progressBar2.progress = 0
            else binding.progressBar2.progress = schoolEvent.countCompletedTask * 100 / schoolEvent.countTask
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