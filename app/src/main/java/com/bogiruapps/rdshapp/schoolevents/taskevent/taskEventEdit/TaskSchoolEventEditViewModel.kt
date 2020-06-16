package com.bogiruapps.rdshapp.schoolevents.taskevent.taskEventEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEvent
import kotlinx.coroutines.launch

class TaskSchoolEventEditViewModel(
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository)
    : ViewModel() {

    private val _openTaskEventFragment = MutableLiveData<Event<Unit>>()
    val openTaskEventFragment: LiveData<Event<Unit>> = _openTaskEventFragment

    private val _users = MutableLiveData<List<User?>>()
    val users: LiveData<List<User?>> = _users

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val event = schoolEventRepository.currentEvent.value!!
    private val user = userRepository.currentUser.value!!

    val taskSchoolEvent: TaskSchoolEvent = TaskSchoolEvent()

    fun createTaskEvent() {
        if (taskSchoolEvent.title == "" || taskSchoolEvent.description == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            viewModelScope.launch {
                when (schoolEventRepository.createTaskSchoolEvent(user, taskSchoolEvent)) {
                    is Result.Success -> {
                        schoolEventRepository.currentEvent.value!!.countTask++
                        when (schoolEventRepository.updateSchoolEvent(user, event)) {
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
