package com.bogiruapps.rdshapp.events.edit_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch
import java.util.*

class EventEditViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    val event = userRepository.currentEvent.value

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        userRepository.currentEvent.value!!.deadline = Date(year, month, dayOfMonth)
    }

    fun updateEvent(event: SchoolEvent) {
        when (userRepository.stateEvent.value) {
            State.CREATE -> createEvent(event)
            State.EDIT -> editEvent(event)
        }
    }

    private fun createEvent(event: SchoolEvent) {
        viewModelScope.launch {
            when(userRepository.createEvent(event)) {
                is Result.Success -> {
                    userRepository.currentEvent.value = event
                    openSchoolEventFragment()
                }
            }
        }
    }

    private fun editEvent(event: SchoolEvent) {
        viewModelScope.launch {
            userRepository.updateEvent(event)
            openSchoolEventFragment()
        }
    }

    private fun openSchoolEventFragment() {
        _openSchoolEventFragment.value = Event(Unit)
    }
}