package com.bogiruapps.rdshapp.events.detailevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openTaskEventRecyclerView = MutableLiveData<Event<Unit>>()
    val openTaskEventFragment: LiveData<Event<Unit>> = _openTaskEventRecyclerView

    private val _openEventFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventFragmentEvent: LiveData<Event<Unit>> = _openEventFragmentEvent

    private val _openEventEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventEditFragmentEvent: LiveData<Event<Unit>> = _openEventEditFragmentEvent

    private val _openDialogDeleteEvent = MutableLiveData<Event<Unit>>()
    val openDialogDeleteEvent: LiveData<Event<Unit>> = _openDialogDeleteEvent

    private val _showSnackbar = MutableLiveData<Event<String>>()
    val showSnackbar: LiveData<Event<String>> = _showSnackbar

    val user = userRepository.currentUser.value!!
    val schoolEvent = eventRepository.currentEvent.value!!

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun deleteSchoolEvent() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (eventRepository.deleteEvent(user)) {
                is Result.Success -> {
                    when (chatRepository.deleteChat(
                        user,
                        schoolEvent)) {
                        is Result.Success -> _dataLoading.value = false
                    }
                }
            }
        }
    }

    fun showEditEventFragment() {
        if (user.email == schoolEvent.author.email) {
            _openEventEditFragmentEvent.value = Event(Unit)
            eventRepository.stateEvent.value = State.EDIT
        } else {
            _showSnackbar.value = Event("Право редактирование предоставлено только автору объявления")
        }

    }

    fun showDialogDeleteSchoolEvent() {
        if (user.name == schoolEvent.author.name)  {
            _openDialogDeleteEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = Event("Право удаления предоставлено только автору объявления")
        }
    }
}
