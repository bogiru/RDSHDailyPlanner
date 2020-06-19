package com.bogiruapps.rdshapp.schoolevents.taskevent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.TasksSchoolEventItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TaskSchoolEventAdapter(
    options : FirestoreRecyclerOptions<TaskSchoolEvent>,
    private val viewModelSchool: TaskSchoolEventViewModel
) :
    FirestoreRecyclerAdapter<TaskSchoolEvent, TaskSchoolEventAdapter.TaskSchoolEventViewHolder>(options) {

    override fun onBindViewHolder(
        holderSchool:  TaskSchoolEventViewHolder,
        position: Int,
        taskEvent: TaskSchoolEvent
    ) {
        holderSchool.bind(viewModelSchool, taskEvent)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskSchoolEventViewHolder {
        return TaskSchoolEventViewHolder.from(parent)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class TaskSchoolEventViewHolder(
        private val binding: TasksSchoolEventItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TaskSchoolEventViewModel, taskEvent: TaskSchoolEvent) {
            binding.taskEvent = taskEvent
            binding.viewModel = viewModel

            if (taskEvent.user!!.id != viewModel.user.id) {
                binding.ckeckBoxTaskCompleted.isEnabled = false
            }

            if (taskEvent.completed) {
                binding.taskEventStatus.text = R.string.no_internet.toString()
            } else {
                binding.taskEventStatus.text = R.string.status_await_completion.toString()
            }
            var numberClick = 1

            binding.taskEventItemCardView.setOnClickListener {
                if (numberClick % 2 == 0) hideDetailSchoolEvent()
                else showDetailSchoolEvent()

                numberClick++
            }
            binding.executePendingBindings()
        }

        private fun showDetailSchoolEvent() {
            binding.taskEventMoreDetailsImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp)
            binding.taskEventDescription.visibility = View.VISIBLE
            binding.ckeckBoxTaskCompleted.visibility = View.VISIBLE
        }

        private fun hideDetailSchoolEvent() {
            binding.taskEventMoreDetailsImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp)
            binding.taskEventDescription.visibility = View.GONE
            binding.ckeckBoxTaskCompleted.visibility = View.GONE
        }

        companion object {
            fun from(parent: ViewGroup): TaskSchoolEventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TasksSchoolEventItemBinding.inflate(layoutInflater, parent, false)
                return TaskSchoolEventViewHolder(binding)
            }
        }
    }
}