package com.bogiruapps.rdshapp.schoolevents.taskevent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class TaskSchoolEventViewModel(
    private val application: Application,
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository)
    : ViewModel() {

    private val _openTaskSchoolEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskSchoolEventEdit: LiveData<Event<Unit>> = _openTaskSchoolEventEdit

    private val _queryTaskSchoolEvent = MutableLiveData<Query>()
    val queryTasksSchoolEvent: LiveData<Query> = _queryTaskSchoolEvent

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    val schoolEvent = schoolEventRepository.currentEvent.value!!
    val user = userRepository.currentUser.value!!

    init {
        fetchFirestoreRecyclerQueryTasksSchoolEvent()
    }

    fun checkUserIsAuthorSchoolEvent() =  schoolEvent.author.id == user.id

    private fun fetchFirestoreRecyclerQueryTasksSchoolEvent() {
        viewModelScope.launch {
            when (val result =
                schoolEventRepository.fetchFirestoreRecyclerQueryTasksSchoolEvent(user)) {
                is Result.Success -> {
                    _queryTaskSchoolEvent.value = result.data
                    _dataLoading.value = false
                }

                is Result.Canceled ->
                    showSnackbar( application.resources
                        .getString(R.string.error_fetch_school_event_tasks_list))

                is Result.Error ->
                    showSnackbar(application.resources
                        .getString(R.string.error_fetch_school_event_tasks_list))
            }
        }
    }

    fun taskCompleted(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            if (checkUserIsAuthorSchoolEvent()) {
                taskSchoolEvent.completed = !(taskSchoolEvent.completed)
                _dataLoading.value = true

                when (schoolEventRepository.updateTaskSchoolEvent(user, taskSchoolEvent)) {
                    is Result.Success -> {
                        if (taskSchoolEvent.completed) {
                            schoolEvent.countCompletedTask++
                            user.score++
                        } else {
                            schoolEvent.countCompletedTask--
                            user.score--
                        }

                        when (schoolEventRepository.updateSchoolEvent(user, schoolEvent)) {
                            is Result.Success -> {
                                schoolEventRepository.currentEvent.value = schoolEvent
                                when (userRepository.updateUser(user)) {
                                    is Result.Success ->  {
                                        userRepository.currentUser.value = user
                                        _dataLoading.value = false
                                    }
                                }
                        }
                        }
                    }

                    is Result.Canceled ->
                        showSnackbar( application.resources
                            .getString(R.string.error_update_task_school_event))

                    is Result.Error ->
                        showSnackbar(application.resources
                            .getString(R.string.error_update_task_school_event))
                }
            }
        }
    }

    fun showTaskSchoolEventEdit() {
        _openTaskSchoolEventEdit.value = Event(Unit)
    }

    fun showSnackbar(message: String) {
        _showSnackbar.value = message
    }

}