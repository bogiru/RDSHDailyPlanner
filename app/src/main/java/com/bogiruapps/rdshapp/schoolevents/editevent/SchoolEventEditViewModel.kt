package com.bogiruapps.rdshapp.schoolevents.editevent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch
import java.util.*

class SchoolEventEditViewModel(
    private val application: Application,
    userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    val user = userRepository.currentUser.value!!
    val schoolEvent = schoolEventRepository.currentEvent.value!!

    private val _imageIndex = MutableLiveData(schoolEvent.imageIndex)
    val imageIndex: LiveData<Int> = _imageIndex

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _showDatePickerDialog = MutableLiveData<Event<Unit>>()
    val showDatePickerDialog: MutableLiveData<Event<Unit>> = _showDatePickerDialog

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun checkCreateSchoolEventStatus(): Boolean = schoolEventRepository.stateEvent.value == State.CREATE

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        schoolEvent.deadline = Date(year, month, dayOfMonth)
    }

    fun updateSchoolEvent(event: SchoolEvent) {
        if (event.title == "" || event.description == "") {

            _showSnackbar.value = application.resources
                .getString(R.string.error_not_all_fields_are_filled)
        } else {
            _dataLoading.value = true
            when (schoolEventRepository.stateEvent.value) {
                State.CREATE -> createSchoolEvent(event)
                State.EDIT -> editSchoolEvent(event)
            }
        }
    }

    fun showDatePickerDialog() {
        _showDatePickerDialog.value = Event(Unit)
    }

    fun setNextImageSchoolEvent() {
        _imageIndex.value = ( _imageIndex.value!! + 1) % 45
    }

    fun setPreviousImageSchoolEvent() {
        _imageIndex.value = (_imageIndex.value!!  - 1)
        if (_imageIndex.value!!  < 0) _imageIndex.value = 44
    }

    private fun createSchoolEvent(event: SchoolEvent) {
        viewModelScope.launch {
            event.author = user
            event.imageIndex = _imageIndex.value!!

            when(schoolEventRepository.createSchoolEvent(user, event)) {
                is Result.Success -> {
                    schoolEventRepository.currentEvent.value = event
                    val chat = Chat(
                        event.id,
                        event.title,
                        Message(application.resources.getString(R.string.there_are_not_message)),
                        event.imageIndex)
                    when (chatRepository.createChat(user, chat)) {
                        is Result.Success -> {
                            chatRepository.currentChat.value = chat
                            _dataLoading.value = false
                            openSchoolEventFragment()
                        }
                    }
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_create_event_to_db)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_create_event_to_db)

            }
        }
    }

    private fun editSchoolEvent(event: SchoolEvent) {
        event.imageIndex = _imageIndex.value!!
        
        viewModelScope.launch {
            when (schoolEventRepository.updateSchoolEvent(user, event)) {
                is Result.Success -> {

                    schoolEventRepository.currentEvent.value = event

                    val tempChat = chatRepository.currentChat.value!!
                    tempChat.title = event.title
                    tempChat.imageIndex = _imageIndex.value!!

                    when (chatRepository.updateChat(user, tempChat)) {
                        is Result.Success -> {
                            chatRepository.currentChat.value = tempChat
                            _dataLoading.value = false
                            openSchoolEventFragment()
                        }

                        is Result.Canceled ->
                            _showSnackbar.value = application.resources
                                .getString(R.string.error_update_chats_to_db)

                        is Result.Error ->
                            _showSnackbar.value = application.resources
                                .getString(R.string.error_update_chats_to_db)
                    }
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_update_event_to_db)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_update_event_to_db)
            }
        }
    }

    private fun openSchoolEventFragment() {
        _openSchoolEventFragment.value = Event(Unit)
    }
}