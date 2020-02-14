package com.bogiruapps.rdshapp.events.tasksEvent

import android.graphics.Color
import android.util.Log
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

            if (taskEvent.user!!.email == FirebaseAuth.getInstance().currentUser!!.email) {
                binding.ckeckBoxTaskCompleted.visibility = View.GONE
            }

            if (taskEvent.compl) {
                binding.statusTaskEvent.text = "Статус: Выполнен"
               // binding.statusTaskEvent.setTextColor(3328)
            } else {
                binding.statusTaskEvent.text = "Статус: Ожидает выполнения"
                //binding.statusTaskEvent.setTextColor(90800)
            }
            var numberClick = 1

            binding.cardView.setOnClickListener {
                if (numberClick % 2 == 0) hideDetailEvent(viewModel)
                else showDetailEvent(viewModel, taskEvent)

                numberClick++
            }

            /* binding.cardView.setOnLongClickListener {
                     viewModel.showAlertDialogDelete(taskEvent)
                 return@setOnLongClickListener true
             }
*/
            binding.executePendingBindings()

        }

        private fun showDetailEvent(viewModel: TaskEventViewModel, taskEvent: TaskEvent) {
            binding.imageView5.setImageResource(R.drawable.ic_expand_less_black_48dp)
            binding.descriptionTaskEvent.visibility = View.VISIBLE
            binding.ckeckBoxTaskCompleted.visibility = View.VISIBLE
        }

        private fun hideDetailEvent(viewModel: TaskEventViewModel) {
            binding.imageView5.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp)
            binding.descriptionTaskEvent.visibility = View.GONE
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