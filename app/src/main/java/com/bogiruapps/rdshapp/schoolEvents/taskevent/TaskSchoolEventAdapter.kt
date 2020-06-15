package com.bogiruapps.rdshapp.schoolEvents.taskevent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.TasksEventItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class TaskSchoolEventAdapter(
    options : FirestoreRecyclerOptions<TaskSchoolEvent>,
    private val viewModelSchool: TaskSchoolEventViewModel
) :
    FirestoreRecyclerAdapter<TaskSchoolEvent, TaskSchoolEventAdapter.TaskSchoolEventViewHolder>(options) {

    override fun onBindViewHolder(holderSchool:  TaskSchoolEventViewHolder, position: Int, taskEvent: TaskSchoolEvent) {
        holderSchool.bind(viewModelSchool, taskEvent)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskSchoolEventViewHolder {
        return TaskSchoolEventViewHolder.from(parent)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class TaskSchoolEventViewHolder(private val binding: TasksEventItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModelSchool: TaskSchoolEventViewModel, taskEvent: TaskSchoolEvent) {
            binding.taskEvent = taskEvent
            binding.viewModel = viewModelSchool

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
                if (numberClick % 2 == 0) hideDetailSchoolEvent()
                else showDetailSchoolEvent()

                numberClick++
            }
            binding.executePendingBindings()
        }

        private fun showDetailSchoolEvent() {
            binding.taskEventMoreDetailsImageView.setImageResource(R.drawable.ic_expand_less_black_48dp)
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
                val binding = TasksEventItemBinding.inflate(layoutInflater, parent, false)
                return TaskSchoolEventViewHolder(binding)
            }
        }
    }
}