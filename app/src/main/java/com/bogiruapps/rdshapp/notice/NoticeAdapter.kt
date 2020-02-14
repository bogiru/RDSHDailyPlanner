package com.bogiruapps.rdshapp.notice

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.R.drawable.header
import com.bogiruapps.rdshapp.databinding.NoticeItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class NoticeAdapter(
        options : FirestoreRecyclerOptions<Notice>,
        private val viewModel: NoticeViewModel) :
    FirestoreRecyclerAdapter<Notice, NoticeAdapter.NoticeViewHolder>(options) {

    override fun onBindViewHolder(p0: NoticeViewHolder, p1: Int, p2: Notice) {
        p2.id = snapshots.getSnapshot(p1).reference.id
        p0.bind(viewModel, p2)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
            return NoticeViewHolder.from(parent)
        }

        class NoticeViewHolder(private val binding: NoticeItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(viewModel: NoticeViewModel, notice: Notice) {
                binding.notice = notice
                binding.constraintLayout.background
                binding.viewModel = viewModel

                binding.executePendingBindings()

            }
            companion object {
                fun from(parent: ViewGroup): NoticeViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = NoticeItemBinding.inflate(layoutInflater, parent, false)
                    return NoticeViewHolder(binding)
                }
            }
        }
}