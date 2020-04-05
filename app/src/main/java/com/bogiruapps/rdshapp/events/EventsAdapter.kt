package com.bogiruapps.rdshapp.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.EventsItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.lang.Math.random

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

        val images = listOf(
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p9,
            R.drawable.p10,
            R.drawable.p11,
            R.drawable.p12,
            R.drawable.p13,
            R.drawable.p14,
            R.drawable.p15,
            R.drawable.p16,
            R.drawable.p17,
            R.drawable.p18,
            R.drawable.p19,
            R.drawable.p20,
            R.drawable.p21
        )


        fun bind(viewModel: EventsViewModel, schoolEvent: SchoolEvent) {
            binding.event = schoolEvent
            binding.viewModel = viewModel
            binding.imageView3.setImageResource(images.random())
           if (schoolEvent.countTask == 0) {
               binding.eventProgressBar.setDonut_progress("0")
           } else {
               binding.eventProgressBar.setDonut_progress("${schoolEvent.countCompletedTask * 100 / schoolEvent.countTask}")
               //binding.eventProgressTextView.text = "${schoolEvent.countCompletedTask * 100 / schoolEvent.countTask}%"
           }
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