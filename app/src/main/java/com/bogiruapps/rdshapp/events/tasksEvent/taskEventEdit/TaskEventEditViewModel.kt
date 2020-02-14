package com.bogiruapps.rdshapp.events.tasksEvent.taskEventEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.Result
import com.bogiruapps.rdshapp.User
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import kotlinx.coroutines.launch

class TaskEventEditViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _openTaskEventFragment = MutableLiveData<Event<Unit>>()
    val openTaskEventFragment: LiveData<Event<Unit>> = _openTaskEventFragment

    private val _users = MutableLiveData<List<User?>>()
    val users: LiveData<List<User?>> = _users

    val event = userRepository.currentEvent.value

    val taskEvent: TaskEvent = TaskEvent()

    fun createTaskEvent() {
        viewModelScope.launch {
            when(userRepository.createTaskEvent(taskEvent)) {
                is Result.Success -> {
                    userRepository.currentEvent.value!!.amountTask++
                    when(userRepository.updateEvent(userRepository.currentEvent.value!!)) {
                        is Result.Success ->   showTaskEventFragment()
                    }
                }
            }
        }
    }

    init {
        initUsers()
    }

   /* fun editTaskEvent(event: SchoolEvent) {
        if (event.id == "") createTaskEvent(event)
        else viewModelScope.launch {
            userRepository.updateEvent(event)
            showTaskEventFragment()
        }
    }*/

    private fun initUsers() {
        viewModelScope.launch {
            when (val result = userRepository.fetchStudents()) {
                is Result.Success -> {
                    _users.value = result.data
                }
            }
        }
    }

    private fun showTaskEventFragment() {
        _openTaskEventFragment.value = Event(Unit)
    }

}
