package com.bogiruapps.rdshapp.chats.chatroomevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.SchoolEventChatRoomItemBinding
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class ChatRoomAdapter(
    options : FirestoreRecyclerOptions<Message>,
    private val viewModel: ChatRoomViewModel
) :
    FirestoreRecyclerAdapter<Message, ChatRoomAdapter.EventsChatRoomViewHolder>(options) {

    override fun onBindViewHolder(p0: EventsChatRoomViewHolder, p1: Int, p2: Message) {
        p0.bind(viewModel, p2)

    }

    override fun onChildChanged(
        type: ChangeEventType,
        snapshot: DocumentSnapshot,
        newIndex: Int,
        oldIndex: Int
    ) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex)
        viewModel.updateEventChatRoomRecyclerView()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsChatRoomViewHolder {
        return EventsChatRoomViewHolder.from(parent)
    }

    class EventsChatRoomViewHolder(private val binding: SchoolEventChatRoomItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ChatRoomViewModel, message: Message) {
            binding.message = message
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EventsChatRoomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SchoolEventChatRoomItemBinding.inflate(layoutInflater, parent, false)
                return EventsChatRoomViewHolder(binding)
            }
        }
    }
}