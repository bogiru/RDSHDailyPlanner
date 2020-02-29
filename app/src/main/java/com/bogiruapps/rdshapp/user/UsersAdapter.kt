package com.bogiruapps.rdshapp.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.UserItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UsersAdapter (
    options: FirestoreRecyclerOptions<User>,
    private val viewModel: UsersViewModel
) : FirestoreRecyclerAdapter<User, UsersAdapter.UserViewHolder>(options) {

    override fun onBindViewHolder(p0: UserViewHolder, p1: Int, p2: User) {
        p0.bind(viewModel, p2, p1 + 1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: UsersViewModel, user: User, numberUser: Int) {
            binding.user = user
            binding.viewModel = viewModel
            binding.numberUser.text = numberUser.toString()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }
}