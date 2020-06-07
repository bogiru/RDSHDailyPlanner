package com.bogiruapps.rdshapp.events.tasksEvent.taskEventEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import kotlinx.coroutines.launch

class TaskEventEditViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository)
    : ViewModel() {

    private val _openTaskEventFragment = MutableLiveData<Event<Unit>>()
    val openTaskEventFragment: LiveData<Event<Unit>> = _openTaskEventFragment

    private val _users = MutableLiveData<List<User?>>()
    val users: LiveData<List<User?>> = _users

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    val event = eventRepository.currentEvent.value

    val taskEvent: TaskEvent = TaskEvent()

    fun createTaskEvent() {
        if (taskEvent.title == "" || taskEvent.description == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            viewModelScope.launch {
                when (eventRepository.createTaskEvent(userRepository.currentUser.value!!, taskEvent)) {
                    is Result.Success -> {
                        eventRepository.currentEvent.value!!.countTask++
                        when (eventRepository.updateEvent(userRepository.currentUser.value!!, eventRepository.currentEvent.value!!)) {
                            is Result.Success -> showTaskEventFragment()
                        }
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
            userRepository.editEvent(event)
            showDetailEventFragment()
        }
    }*/

    private fun initUsers() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUsers()) {
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
