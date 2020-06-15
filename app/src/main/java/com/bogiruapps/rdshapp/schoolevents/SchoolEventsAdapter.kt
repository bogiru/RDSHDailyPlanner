package com.bogiruapps.rdshapp.schoolevents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.SchoolEventsItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SchoolEventsAdapter(
    options : FirestoreRecyclerOptions<SchoolEvent>,
    private val viewModel: SchoolEventsViewModel
) : FirestoreRecyclerAdapter<SchoolEvent, SchoolEventsAdapter.EventsViewHolder>(options) {

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int, event: SchoolEvent) {
        holder.bind(viewModel, event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder.from(parent)
    }

    class EventsViewHolder(private val binding: SchoolEventsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SchoolEventsViewModel, schoolEvent: SchoolEvent) {
            binding.event = schoolEvent
            binding.viewModel = viewModel
            setSchoolEventProgressBar(schoolEvent)
            binding.executePendingBindings()
        }

        private fun setSchoolEventProgressBar(schoolEvent: SchoolEvent) {
            val progress =
                if (schoolEvent.countTask == 0) 0
                else schoolEvent.countCompletedTask * 100 / schoolEvent.countTask

            binding.eventProgressBar.setDonut_progress("$progress")
        }

        companion object {
            fun from(parent: ViewGroup): EventsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SchoolEventsItemBinding.inflate(layoutInflater, parent, false)
                return EventsViewHolder(binding)
            }
        }
    }
}