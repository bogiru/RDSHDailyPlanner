package com.bogiruapps.rdshapp.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.ChatItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatsAdapter(
    options: FirestoreRecyclerOptions<Chat>,
    private val viewModel: ChatsViewModel
) : FirestoreRecyclerAdapter<Chat, ChatsAdapter.ChatsViewHolder>(options) {

    override fun onBindViewHolder(p0: ChatsViewHolder, p1: Int, p2: Chat) {
        p0.bind(viewModel, p2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder.from(parent)
    }

    class ChatsViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ChatsViewModel, chat: Chat) {
            binding.chat = chat
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ChatsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChatItemBinding.inflate(layoutInflater, parent, false)
                return ChatsViewHolder(binding)
            }
        }
    }
}