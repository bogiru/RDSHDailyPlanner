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

    init {
        fetchUsers()
    }

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

                    is Result.Canceled ->
                        _showSnackbar.value = "Ошибка при создании задачи. Попробуйте снова"

                    is Result.Error ->
                        _showSnackbar.value = "Ошибка при создании задачи. Попробуйте снова"
                }
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            when (val result = userRepository.fetchUsers()) {
                is Result.Success -> _users.value = result.data

                is Result.Canceled ->
                    _showSnackbar.value = "Ошибка при получении учеников. Попробуйте снова"

                is Result.Canceled ->
                    _showSnackbar.value = "Ошибка при получении учеников. Попробуйте снова"
            }
        }
    }

    private fun showTaskEventFragment() {
        _openTaskEventFragment.value = Event(Unit)
    }

}