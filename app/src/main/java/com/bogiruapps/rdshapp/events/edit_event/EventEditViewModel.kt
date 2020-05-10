package com.bogiruapps.rdshapp.events.edit_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.chats.chat_room_event.Message
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.*

class EventEditViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _indexImage = MutableLiveData<Int>()
    val indexImage: LiveData<Int> = _indexImage

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _showDatePickerDialog = MutableLiveData<Event<Unit>>()
    val showDatePickerDialog: MutableLiveData<Event<Unit>> = _showDatePickerDialog

    val event = userRepository.currentEvent.value

    fun checkCreateEventStatus(): Boolean = userRepository.stateEvent.value == State.CREATE

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        event!!.deadline = Date(year, month, dayOfMonth)
    }

    fun updateEvent(event: SchoolEvent) {
        if (event.title == "" || event.description == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            when (userRepository.stateEvent.value) {
                State.CREATE -> createEvent(event)
                State.EDIT -> editEvent(event)
            }
        }
    }

    fun showDatePickerDialog() {
        _showDatePickerDialog.value = Event(Unit)
    }

    fun setNextImageEvent() {
        event!!.indexImage = (event.indexImage + 1) % 25
        _indexImage.value = event.indexImage
    }

    fun setLastImageEvent() {
        event!!.indexImage = (event.indexImage - 1)
        if (event.indexImage < 0) event!!.indexImage = 24
        _indexImage.value = event.indexImage
    }

    private fun createEvent(event: SchoolEvent) {
        viewModelScope.launch {
            event.author = userRepository.currentUser.value!!
            when(userRepository.createEvent(event)) {
                is Result.Success -> {
                    val chat = Chat(event.id, event.title, Message("Сообщений нет"), event.indexImage)
                    when (userRepository.createChat(chat)) {
                        is Result.Success -> {
                            userRepository.currentEvent.value = event
                            openSchoolEventFragment()
                        }
                    }

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