package com.bogiruapps.rdshapp.chats.chatroomevent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.MessageItemBinding
import com.bogiruapps.rdshapp.utils.setDateWithoutTime
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

class ChatRoomAdapter(
    options : FirestoreRecyclerOptions<Message>,
    private val viewModel: ChatRoomViewModel
) :
    FirestoreRecyclerAdapter<Message, ChatRoomAdapter.EventsChatRoomViewHolder>(options) {

    override fun onBindViewHolder(holder: EventsChatRoomViewHolder, position: Int, message: Message) {
        holder.bind(viewModel, message)
    }

    override fun onChildChanged(
        type: ChangeEventType,
        snapshot: DocumentSnapshot,
        newIndex: Int,
        oldIndex: Int
    ) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex)
        viewModel.updateChatRoomRecyclerView()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsChatRoomViewHolder {
        return EventsChatRoomViewHolder.from(parent)
    }

    class EventsChatRoomViewHolder(private val binding: MessageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ChatRoomViewModel, message: Message) {
            binding.message = message
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EventsChatRoomViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MessageItemBinding.inflate(layoutInflater, parent, false)
                return EventsChatRoomViewHolder(binding)
            }
        }
    }
}