package com.bogiruapps.rdshapp.events.detail_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.userData.UserRepository
import com.bogiruapps.rdshapp.data.chatData.ChatRepository
import com.bogiruapps.rdshapp.data.eventData.EventRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val chatRepository: ChatRepository)
    : ViewModel() {

    private val _openTaskEventRecyclerView = MutableLiveData<Event<Unit>>()
    val openTaskEventRecyclerView: LiveData<Event<Unit>> = _openTaskEventRecyclerView

    private val _openEventFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventFragmentEvent: LiveData<Event<Unit>> = _openEventFragmentEvent

    private val _openEventEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventEditFragmentEvent: LiveData<Event<Unit>> = _openEventEditFragmentEvent

    private val _openEventDeleteFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventDeleteFragmentEvent: LiveData<Event<Unit>> = _openEventDeleteFragmentEvent

    private val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>> = _showToast

    val event = eventRepository.currentEvent.value!!

    fun showTaskEventRecyclerView() {
        _openTaskEventRecyclerView.value = Event(Unit)
    }

    fun deleteEvent() {
        viewModelScope.launch {
            when (eventRepository.deleteEvent(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    when (chatRepository.deleteChat(userRepository.currentUser.value!!, eventRepository.currentEvent.value!!)) {

                    }
                }
            }
        }
    }

    fun showEditEventFragment() {
        if (userRepository.currentUser.value!!.email == eventRepository.currentEvent.value!!.author.email) {
            _openEventEditFragmentEvent.value = Event(Unit)
            eventRepository.stateEvent.value = State.EDIT
        } else {
            _showToast.value = Event("Право редактирование предоставлено только автору объявления")
        }

    }

    fun showDeleteEventFragment() {
        if (userRepository.currentUser.value!!.name == eventRepository.currentEvent.value!!.author.name)  {
            _openEventDeleteFragmentEvent.value = Event(Unit)
        } else {
            _showToast.value = Event("Право удаления предоставлено только автору объявления")
        }

    }

}
