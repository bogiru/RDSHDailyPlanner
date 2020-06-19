package com.bogiruapps.rdshapp.schoolevents.taskevent.taskEventEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
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

    private val _openTaskSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openTaskSchoolEventFragment: LiveData<Event<Unit>> = _openTaskSchoolEventFragment

    private val _users = MutableLiveData<List<User?>>()
    val users: LiveData<List<User?>> = _users

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val event = schoolEventRepository.currentEvent.value!!
    private val user = userRepository.currentUser.value!!

    val taskSchoolEvent: TaskSchoolEvent = TaskSchoolEvent()

    init {
        fetchUsers()
    }

    fun createTaskEvent() {
        if (taskSchoolEvent.title == "" || taskSchoolEvent.description == "") {
            _showSnackbar.value = R.string.error_not_all_fields_are_filled.toString()
        } else {
            viewModelScope.launch {
                when (schoolEventRepository.createTaskSchoolEvent(user, taskSchoolEvent)) {
                    is Result.Success -> {
                        schoolEventRepository.currentEvent.value!!.countTask++
                        when (schoolEventRepository.updateSchoolEvent(user, event)) {
                            is Result.Success -> showTaskEventFragment()
                        }
                    }

                    is Result.Canceled ->
                        _showSnackbar.value = R.string.error_create_task_school_event.toString()

                    is Result.Error ->
                        _showSnackbar.value = R.string.error_create_task_school_event.toString()
                }
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUsers()) {
                is Result.Success -> _users.value = result.data

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_users_list_from_current_school.toString()

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_users_list_from_current_school.toString()
            }
        }
    }

    private fun showTaskEventFragment() {
        _openTaskSchoolEventFragment.value = Event(Unit)
    }

}
