package com.bogiruapps.rdshapp.events.tasksEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.data.eventData.EventRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class TaskEventViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository)
    : ViewModel() {

    private val _openTaskEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskEventEdit: LiveData<Event<Unit>> = _openTaskEventEdit

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openTaskEventDeleteFragmentEvent = MutableLiveData<Event<TaskEvent>>()
    val openTaskEventDeleteFragmentEvent: LiveData<Event<TaskEvent>> = _openTaskEventDeleteFragmentEvent

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
                when (eventRepository.updateTaskEvent(userRepository.currentUser.value!!, task)) {
                    is Result.Success -> {
                        if (taskEvent.completed) {
                            eventRepository.currentEvent.value!!.countCompletedTask--
                            userRepository.currentUser.value!!.score--
                        } else {
                            eventRepository.currentEvent.value!!.countCompletedTask++
                            userRepository.currentUser.value!!.score++
                        }
                        eventRepository.updateEvent(userRepository.currentUser.value!!, eventRepository.currentEvent.value!!)
                        userRepository.updateUser(userRepository.currentUser.value!!)
                        userRepository.updateUserInSchool(userRepository.currentUser.value!!)
                    }
                }
            }
        }
    }

    fun showDeleteTaskEventFragment(taskEvent: TaskEvent) {
        if (userRepository.currentUser.value!!.email == eventRepository.currentEvent.value!!.author.email)  {
            _openTaskEventDeleteFragmentEvent.value = Event(taskEvent)
        } else {
            _showSnackbar.value = Event("Право удаления предоставлено только автору мероприятия")
        }
    }

    fun deleteTaskEvent(taskEvent: TaskEvent) {
        viewModelScope.launch {
            eventRepository.deleteTaskEvent(userRepository.currentUser.value!!, taskEvent)
        }
    }

    fun showTaskEditEvent() {
        _openTaskEventEdit.value = Event(Unit)
    }

    fun showSnackbar(message: String) {
        _showSnackbar.value = Event(message)
    }

}