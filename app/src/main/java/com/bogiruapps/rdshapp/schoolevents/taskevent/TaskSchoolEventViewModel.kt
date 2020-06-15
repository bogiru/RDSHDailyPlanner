package com.bogiruapps.rdshapp.schoolevents.taskevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class TaskSchoolEventViewModel(
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository)
    : ViewModel() {

    private val _openTaskSchoolEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskSchoolEventEdit: LiveData<Event<Unit>> = _openTaskSchoolEventEdit

    private val _queryTaskSchoolEvent = MutableLiveData<Query>()
    val query: LiveData<Query> = _queryTaskSchoolEvent

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openTaskSchoolEventDeleteFragmentEvent = MutableLiveData<Event<TaskSchoolEvent>>()
    val openTaskEventDeleteFragmentSchoolEvent: LiveData<Event<TaskSchoolEvent>> = _openTaskSchoolEventDeleteFragmentEvent

    private val _showSnackbar = MutableLiveData<Event<String>>()
    val showSnackbar: LiveData<Event<String>> = _showSnackbar

    val schoolEvent = schoolEventRepository.currentEvent.value!!

    init {
        fetchFirestoreRecyclerQuery()
    }

    fun checkUserIsAuthorSchoolEvent() =  schoolEvent.author.email == userRepository.currentUser.value!!.email

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = schoolEventRepository.fetchFirestoreRecyclerQueryTasksSchoolEvent(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    _queryTaskSchoolEvent.value = result.data
                    _dataLoading.value = false
                }
            }
        }
    }

    fun taskCompleted(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            if (taskSchoolEvent.user!!.email == userRepository.currentUser.value?.email) {
                val task = TaskSchoolEvent(
                    taskSchoolEvent.id,
                    taskSchoolEvent.title,
                    !(taskSchoolEvent.completed),
                    taskSchoolEvent.description,
                    taskSchoolEvent.user
                )

                _dataLoading.value = true

                when (schoolEventRepository.updateTaskSchoolEvent(userRepository.currentUser.value!!, task)) {
                    is Result.Success -> {
                        if (taskSchoolEvent.completed) {
                            schoolEventRepository.currentEvent.value!!.countCompletedTask--
                            userRepository.currentUser.value!!.score--
                        } else {
                            schoolEventRepository.currentEvent.value!!.countCompletedTask++
                            userRepository.currentUser.value!!.score++
                        }
                        schoolEventRepository.updateSchoolEvent(userRepository.currentUser.value!!, schoolEventRepository.currentEvent.value!!)
                        userRepository.updateUser(userRepository.currentUser.value!!)
                        _dataLoading.value = false
                    }
                }
            }
        }
    }

    fun deleteTaskSchoolEvent(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            schoolEventRepository.deleteTaskSchoolEvent(userRepository.currentUser.value!!, taskSchoolEvent)
        }
    }

    fun showTaskSchoolEventEdit() {
        _openTaskSchoolEventEdit.value = Event(Unit)
    }

    fun showSnackbar(message: String) {
        _showSnackbar.value = Event(message)
    }

}