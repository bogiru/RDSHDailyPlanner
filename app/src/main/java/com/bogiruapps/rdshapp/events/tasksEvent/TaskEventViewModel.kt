package com.bogiruapps.rdshapp.events.tasksEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.UserRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.launch

class TaskEventViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openTaskEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskEventEdit: LiveData<Event<Unit>> = _openTaskEventEdit

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<TaskEvent> {
        return userRepository.fetchFirestoreRecyclerOptionsTasksEvent()
    }

    val event = userRepository.currentEvent.value!!


    fun taskCompleted(taskEvent: TaskEvent) {
        viewModelScope.launch {
            if (taskEvent.user!!.email == userRepository.currentUser.value?.email) {
                val task = TaskEvent(
                    taskEvent.id,
                    taskEvent.title,
                    !(taskEvent.completed),
                    taskEvent.description,
                    taskEvent.user
                )
                when (userRepository.updateTaskEvent(task)) {
                    is Result.Success -> {
                        if (taskEvent.completed) userRepository.currentEvent.value!!.countCompletedTask--
                        else userRepository.currentEvent.value!!.countCompletedTask++
                        userRepository.updateEvent(userRepository.currentEvent.value!!)
                    }
                }
            }
        }
    }

    fun showTaskEditEvent() {
        _openTaskEventEdit.value = Event(Unit)
    }

}