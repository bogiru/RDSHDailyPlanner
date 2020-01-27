package com.bogiruapps.rdshapp.events.tasksEvent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.databinding.EventsItemBinding
import com.bogiruapps.rdshapp.databinding.TasksEventItemBinding
import com.bogiruapps.rdshapp.events.EventViewModel
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TaskEventAdapter(
    options : FirestoreRecyclerOptions<TaskEvent>,
    private val viewModel: TaskEventViewModel
) :
    FirestoreRecyclerAdapter<TaskEvent, TaskEventAdapter.TaskEventViewHolder>(options) {

    override fun onBindViewHolder(p0:  TaskEventViewHolder, p1: Int, p2: TaskEvent) {
        p2.id = snapshots.getSnapshot(p1).reference.id
        p0.bind(viewModel, p2)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskEventViewHolder {
        return TaskEventViewHolder.from(parent)
    }

    class TaskEventViewHolder(private val binding: TasksEventItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TaskEventViewModel, taskEvent: TaskEvent) {
            binding.taskEvent = taskEvent
            binding.viewModel = viewModel
            var numberClick = 1

            binding.cardView.setOnClickListener {
                if (numberClick % 2 == 0) hideDetailEvent(viewModel)
                else showDetailEvent(viewModel, taskEvent)
                numberClick++
            }

            /* binding.cardView.setOnLongClickListener {
                 if (viewModel.checkAuthorNotice(notice.author)) {
                     viewModel.showAlertDialogDelete(notice)
                 }
                 return@setOnLongClickListener true
             }*/

            binding.executePendingBindings()

        }

        private fun showDetailEvent(viewModel: TaskEventViewModel, taskEvent: TaskEvent) {
            binding.rdshImage.visibility = View.GONE
            binding.descriptionTaskEvent.visibility = View.VISIBLE
            binding.ckeckBoxTaskCompleted.visibility = View.VISIBLE
            binding.dividingLineTaskEvent.visibility = View.VISIBLE
            binding.eventFub.visibility = View.VISIBLE
        }

        private fun hideDetailEvent(viewModel: TaskEventViewModel) {
            binding.rdshImage.visibility = View.VISIBLE
            binding.descriptionTaskEvent.visibility = View.GONE
            binding.ckeckBoxTaskCompleted.visibility = View.GONE
            binding.dividingLineTaskEvent.visibility = View.GONE
            binding.eventFub.visibility = View.GONE
        }

        companion object {
            fun from(parent: ViewGroup): TaskEventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TasksEventItemBinding.inflate(layoutInflater, parent, false)
                return TaskEventViewHolder(binding)
            }
        }
    }
}