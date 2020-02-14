package com.bogiruapps.rdshapp.events.tasksEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.Result
import com.bogiruapps.rdshapp.UserRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import io.grpc.okhttp.internal.Util
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
                    !(taskEvent.compl),
                    taskEvent.description,
                    taskEvent.user
                )
                when (userRepository.updateTaskEvent(task)) {
                    is Result.Success -> {
                        if (taskEvent.compl) userRepository.currentEvent.value!!.amountCompletedTask--
                        else userRepository.currentEvent.value!!.amountCompletedTask++
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