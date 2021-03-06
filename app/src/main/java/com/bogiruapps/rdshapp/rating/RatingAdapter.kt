package com.bogiruapps.rdshapp.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.RatingItemBinding
import com.bogiruapps.rdshapp.user.User
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class RatingAdapter (
    options: FirestoreRecyclerOptions<User>,
    private val viewModel: RatingViewModel
) : FirestoreRecyclerAdapter<User, RatingAdapter.RatingViewHolder>(options) {

    override fun onBindViewHolder(viewHolder: RatingViewHolder, position: Int, user: User) {
        viewHolder.bind(viewModel, user, position + 1)
    }

    override fun onChildChanged(
        type: ChangeEventType,
        snapshot: DocumentSnapshot,
        newIndex: Int,
        oldIndex: Int
    ) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        return RatingViewHolder.from(parent)
    }

    class RatingViewHolder(private val binding: RatingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: RatingViewModel, user: User, position: Int) {
            binding.user = user
            binding.viewModel = viewModel
            binding.place = position
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RatingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RatingItemBinding.inflate(layoutInflater, parent, false)
                return RatingViewHolder(binding)
            }
        }
    }
}