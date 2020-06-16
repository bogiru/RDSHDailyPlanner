package com.bogiruapps.rdshapp.schoolevents.editevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class SchoolEventEditViewModel(
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _imageIndex = MutableLiveData<Int>()
    val imageIndex: LiveData<Int> = _imageIndex

    private val _openSchoolEventFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventFragment: LiveData<Event<Unit>> = _openSchoolEventFragment

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _showDatePickerDialog = MutableLiveData<Event<Unit>>()
    val showDatePickerDialog: MutableLiveData<Event<Unit>> = _showDatePickerDialog

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val user = userRepository.currentUser.value!!
    val schoolEvent = schoolEventRepository.currentEvent.value!!

    fun checkCreateSchoolEventStatus(): Boolean = schoolEventRepository.stateEvent.value == State.CREATE

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        //schoolEvent!!.deadline = Calendar.getInstance().set(year, month, dayOfMonth)
    }

    fun updateSchoolEvent(event: SchoolEvent) {
        if (event.title == "" || event.description == "") {
            _showSnackbar.value = "Не все поля заполнены"
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
        schoolEvent.imageIndex = (schoolEvent.imageIndex + 1) % 25
        _imageIndex.value = schoolEvent.imageIndex
    }

    fun setPreviousImageSchoolEvent() {
        schoolEvent.imageIndex = (schoolEvent.imageIndex - 1)
        if (schoolEvent.imageIndex < 0) schoolEvent.imageIndex = 24
        _imageIndex.value = schoolEvent.imageIndex
    }

    private fun createSchoolEvent(event: SchoolEvent) {
        viewModelScope.launch {
            event.author = user
            when(schoolEventRepository.createSchoolEvent(user, event)) {
                is Result.Success -> {
                    schoolEventRepository.currentEvent.value = event
                    val chat = Chat(
                        event.id,
                        event.title,
                        Message("Сообщений нет"),
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
                    _showSnackbar.value = "Ошибка при создании мероприятия. Попробуйте снова"

                is Result.Error ->
                    _showSnackbar.value = "Ошибка при создании мероприятия. Попробуйте снова"

            }
        }
    }

    private fun editSchoolEvent(event: SchoolEvent) {
        viewModelScope.launch {
            when (schoolEventRepository.updateSchoolEvent(user, event)) {
                is Result.Success -> {
                    val tempChat = chatRepository.currentChat.value!!
                    tempChat.title = event.title
                    tempChat.indexImage = _imageIndex.value!!

                    when (chatRepository.updateChat(user, tempChat)) {
                        is Result.Success -> {
                            chatRepository.currentChat.value = tempChat
                            _dataLoading.value = false
                            openSchoolEventFragment()
                        }

                        is Result.Canceled ->
                            _showSnackbar.value = "Ошибка при обновлении чата. Попробуйте снова"

                        is Result.Error ->
                            _showSnackbar.value = "Ошибка при обновлении чата. Попробуйте снова"
                    }
                }

                is Result.Canceled ->
                    _showSnackbar.value = "Ошибка при обновлении мероприятия. Попробуйте снова"

                is Result.Error ->
                    _showSnackbar.value = "Ошибка при обновлении мероприятия. Попробуйте снова"
            }
        }
    }

    private fun openSchoolEventFragment() {
        _openSchoolEventFragment.value = Event(Unit)
    }
}