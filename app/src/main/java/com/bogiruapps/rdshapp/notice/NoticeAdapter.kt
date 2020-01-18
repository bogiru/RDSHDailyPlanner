package com.bogiruapps.rdshapp.notice

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                binding.viewModel = viewModel
                var numberClick = 1

                binding.cardView.setOnClickListener {
                    if (numberClick % 2 == 0) hideDetailNotice(viewModel, notice)
                    else showDetailNotice(viewModel, notice)
                    numberClick++
                }

                binding.cardView.setOnLongClickListener {
                    if (viewModel.checkAuthorNotice(notice.author)) {
                        viewModel.showAlertDialogDelete(notice)
                    }
                    return@setOnLongClickListener true
                }

                binding.executePendingBindings()

            }

            private fun showDetailNotice(viewModel: NoticeViewModel,  notice: Notice) {
                binding.rdshImage.visibility = View.GONE
                binding.textNotice.visibility = View.VISIBLE
                if (viewModel.checkAuthorNotice(notice.author)) binding.fubEdit.visibility = View.VISIBLE
                binding.dividingLine.visibility = View.VISIBLE
            }

            private fun hideDetailNotice(viewModel: NoticeViewModel,  notice: Notice) {
                binding.rdshImage.visibility = View.VISIBLE
                binding.textNotice.visibility = View.GONE
                binding.fubEdit.visibility = View.GONE
                binding.dividingLine.visibility = View.GONE
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