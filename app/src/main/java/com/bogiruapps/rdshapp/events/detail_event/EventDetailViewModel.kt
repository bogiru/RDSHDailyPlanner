package com.bogiruapps.rdshapp.events.detail_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
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

    fun showEventDeleteFragmentEvent() {
        _openEventDeleteFragmentEvent.value = Event(Unit)
    }

    fun deleteEvent() {
        viewModelScope.launch {
            when (userRepository.deleteEvent()) {
            }
        }
    }


}
