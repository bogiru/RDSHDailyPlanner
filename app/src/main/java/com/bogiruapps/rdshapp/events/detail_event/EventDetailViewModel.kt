package com.bogiruapps.rdshapp.events.detail_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class EventDetailViewModel(val userRepository: UserRepository) : ViewModel() {
    private val _openTaskEventRecyclerView = MutableLiveData<Event<Unit>>()
    val openTaskEventRecyclerView: LiveData<Event<Unit>> = _openTaskEventRecyclerView

    private val _openEventDeleteFragmentEvent = MutableLiveData<Event<Unit>>()
    val openEventDeleteFragmentEvent: LiveData<Event<Unit>> = _openEventDeleteFragmentEvent

    val event = userRepository.currentEvent.value!!

    fun showTaskEventRecyclerView() {
        _openTaskEventRecyclerView.value = Event(Unit)
    }

    fun setStateEdit() {
        userRepository.stateEvent.value = State.EDIT
    }

    fun deleteEvent() {
        viewModelScope.launch {
            when (userRepository.deleteEvent()) {
            }
        }
    }


}
