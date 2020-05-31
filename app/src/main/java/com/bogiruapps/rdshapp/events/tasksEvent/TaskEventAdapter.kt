package com.bogiruapps.rdshapp.events.tasksEvent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.TasksEventItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class TaskEventAdapter(
    options : FirestoreRecyclerOptions<TaskEvent>,
    private val viewModel: TaskEventViewModel
) :
    FirestoreRecyclerAdapter<TaskEvent, TaskEventAdapter.TaskEventViewHolder>(options) {

    override fun onBindViewHolder(p0:  TaskEventViewHolder, p1: Int, p2: TaskEvent) {
        p0.bind(viewModel, p2)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskEventViewHolder {
        return TaskEventViewHolder.from(parent)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class TaskEventViewHolder(private val binding: TasksEventItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TaskEventViewModel, taskEvent: TaskEvent) {
            binding.taskEvent = taskEvent
            binding.viewModel = viewModel

            if (taskEvent.user!!.email != FirebaseAuth.getInstance().currentUser!!.email) {
                binding.ckeckBoxTaskCompleted.isEnabled = false
            }

            if (taskEvent.completed) {
                binding.taskEventStatus.text = "Статус: Выполнен"
            } else {
                binding.taskEventStatus.text = "Статус: Ожидает выполнения"
            }
            var numberClick = 1

            binding.taskEventItemCardView.setOnClickListener {
                if (numberClick % 2 == 0) hideDetailEvent(viewModel)
                else showDetailEvent(viewModel, taskEvent)

                numberClick++
            }
            binding.executePendingBindings()
        }

        private fun showDetailEvent(viewModel: TaskEventViewModel, taskEvent: TaskEvent) {
            binding.taskEventMoreDetailsImageView.setImageResource(R.drawable.ic_expand_less_black_48dp)
            binding.taskEventDescription.visibility = View.VISIBLE
            binding.ckeckBoxTaskCompleted.visibility = View.VISIBLE
        }

        private fun hideDetailEvent(viewModel: TaskEventViewModel) {
            binding.taskEventMoreDetailsImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp)
            binding.taskEventDescription.visibility = View.GONE
            binding.ckeckBoxTaskCompleted.visibility = View.GONE
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