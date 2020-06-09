package com.bogiruapps.rdshapp.events.editevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class EventEditViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _imageIndex = MutableLiveData<Int>()
    val indexImage: LiveData<Int> = _imageIndex

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _showDatePickerDialog = MutableLiveData<Event<Unit>>()
    val showDatePickerDialog: MutableLiveData<Event<Unit>> = _showDatePickerDialog

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val user = userRepository.currentUser.value!!
    val schoolEvent = eventRepository.currentEvent.value!!

    fun checkCreateEventStatus(): Boolean = eventRepository.stateEvent.value == State.CREATE

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        //schoolEvent!!.deadline = Calendar.getInstance().set(year, month, dayOfMonth)
    }

    fun updateEvent(event: SchoolEvent) {
        if (event.title == "" || event.description == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            _dataLoading.value = true
            when (eventRepository.stateEvent.value) {
                State.CREATE -> createEvent(event)
                State.EDIT -> editSchoolEvent(event)
            }
        }
    }

    fun showDatePickerDialog() {
        _showDatePickerDialog.value = Event(Unit)
    }

    fun setNextImageEvent() {
        schoolEvent.imageIndex = (schoolEvent.imageIndex + 1) % 25
        _imageIndex.value = schoolEvent.imageIndex
    }

    fun setPreviousImageEvent() {
        schoolEvent.imageIndex = (schoolEvent.imageIndex - 1)
        if (schoolEvent.imageIndex < 0) schoolEvent.imageIndex = 24
        _imageIndex.value = schoolEvent.imageIndex
    }

    private fun createEvent(event: SchoolEvent) {
        viewModelScope.launch {
            event.author = user
            when(eventRepository.createEvent(user, event)) {
                is Result.Success -> {
                    eventRepository.currentEvent.value = event
                    val chat = Chat(
                        event.id,
                        event.title,
                        Message("Сообщений нет"),
                        event.imageIndex)
                    when (chatRepository.createChat(userRepository.currentUser.value!!, chat)) {
                        is Result.Success -> {
                            chatRepository.currentChat.value = chat
                            _dataLoading.value = false
                            openSchoolEventFragment()
                        }
                    }
                }
            }
        }
    }

    private fun editSchoolEvent(event: SchoolEvent) {
        viewModelScope.launch {
            when (eventRepository.updateEvent(user, event)) {
                is Result.Success -> {
                    val tempChat = chatRepository.currentChat.value!!
                    tempChat.title = event.title
                    tempChat.indexImage = event.imageIndex

                    when (chatRepository.updateChat(user, tempChat)) {
                        is Result.Success -> {
                            chatRepository.currentChat.value = tempChat
                            _dataLoading.value = false
                            openSchoolEventFragment()
                        }
                    }
                }
            }
        }
    }

    private fun openSchoolEventFragment() {
        _openSchoolEventFragment.value = Event(Unit)
    }
}