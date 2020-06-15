package com.bogiruapps.rdshapp.schoolEvents.taskevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class TaskSchoolEventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository)
    : ViewModel() {

    private val _openTaskEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskEventEdit: LiveData<Event<Unit>> = _openTaskEventEdit

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openTaskEventDeleteFragmentEvent = MutableLiveData<Event<TaskSchoolEvent>>()
    val openTaskEventDeleteFragmentSchoolEvent: LiveData<Event<TaskSchoolEvent>> = _openTaskEventDeleteFragmentEvent

    private val _showSnackbar = MutableLiveData<Event<String>>()
    val showSnackbar: LiveData<Event<String>> = _showSnackbar

    val event = eventRepository.currentEvent.value!!

    init {
        fetchFirestoreRecyclerQuery()
    }

    fun checkUserIsAuthorEvent() =  event.author.email == userRepository.currentUser.value!!.email

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = eventRepository.fetchFirestoreRecyclerQueryTasksEvent(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    _query.value = result.data
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

                when (eventRepository.updateTaskEvent(userRepository.currentUser.value!!, task)) {
                    is Result.Success -> {
                        if (taskSchoolEvent.completed) {
                            eventRepository.currentEvent.value!!.countCompletedTask--
                            userRepository.currentUser.value!!.score--
                        } else {
                            eventRepository.currentEvent.value!!.countCompletedTask++
                            userRepository.currentUser.value!!.score++
                        }
                        eventRepository.updateEvent(userRepository.currentUser.value!!, eventRepository.currentEvent.value!!)
                        userRepository.updateUser(userRepository.currentUser.value!!)
                        _dataLoading.value = false
                    }
                }
            }
        }
    }

    fun showDeleteTaskEventFragment(taskSchoolEvent: TaskSchoolEvent) {
        if (userRepository.currentUser.value!!.email == eventRepository.currentEvent.value!!.author.email)  {
            _openTaskEventDeleteFragmentEvent.value = Event(taskSchoolEvent)
        } else {
            _showSnackbar.value = Event("Право удаления предоставлено только автору мероприятия")
        }
    }

    fun deleteTaskEvent(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            eventRepository.deleteTaskEvent(userRepository.currentUser.value!!, taskSchoolEvent)
        }
    }

    fun showTaskEditEvent() {
        _openTaskEventEdit.value = Event(Unit)
    }

    fun showSnackbar(message: String) {
        _showSnackbar.value = Event(message)
    }

}