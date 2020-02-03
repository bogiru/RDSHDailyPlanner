package com.bogiruapps.rdshapp.events.tasksEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.UserRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.launch

class TaskEventViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<TaskEvent> {
        return userRepository.fetchFirestoreRecyclerOptionsTasksEvent()
    }

    fun taskCompleted(taskEvent: TaskEvent) {
        viewModelScope.launch {
            if (taskEvent.user.email == userRepository.currentUser.value?.email) {
                val task = TaskEvent(
                    taskEvent.id,
                    taskEvent.title,
                    !(taskEvent.compl),
                    taskEvent.description,
                    taskEvent.user
                )
                when (userRepository.updateTaskEvent(task)) {

                }
            }
        }
    }

}