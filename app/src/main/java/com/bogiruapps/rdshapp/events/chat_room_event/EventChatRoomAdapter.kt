package com.bogiruapps.rdshapp.events.chat_room_event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.EventChatRoomItemBinding
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventChatRoomAdapter(
    options : FirestoreRecyclerOptions<Message>,
    private val viewModel: EventChatRoomViewModel
) :
    FirestoreRecyclerAdapter<Message, EventChatRoomAdapter.EventsChatRoomViewHolder>(options) {

    override fun onBindViewHolder(p0: EventsChatRoomViewHolder, p1: Int, p2: Message) {
        p0.bind(viewModel, p2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsChatRoomViewHolder {
        return EventsChatRoomViewHolder.from(parent)
    }

    class EventsChatRoomViewHolder(private val binding: EventChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: EventChatRoomViewModel, message: Message) {
            binding.message = message
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EventsChatRoomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EventChatRoomItemBinding.inflate(layoutInflater, parent, false)
                return EventsChatRoomViewHolder(binding)
            }
        }
    }
}