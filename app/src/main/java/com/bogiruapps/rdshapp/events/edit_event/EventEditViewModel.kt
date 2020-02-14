package com.bogiruapps.rdshapp.events.edit_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.Result
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Month
import java.time.Year
import java.util.*

class EventEditViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    val event = userRepository.currentEvent.value

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        userRepository.currentEvent.value!!.deadline = Date(year, month, dayOfMonth)
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

    fun editEvent(event: SchoolEvent) {
        if (event.id == "") createEvent(event)
        else viewModelScope.launch {
            userRepository.updateEvent(event)
            openSchoolEventFragment()
        }
    }


    private fun openSchoolEventFragment() {
        _openSchoolEventFragment.value = Event(Unit)
    }
}