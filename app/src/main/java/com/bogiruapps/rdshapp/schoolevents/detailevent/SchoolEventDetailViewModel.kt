package com.bogiruapps.rdshapp.schoolevents.detailevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class SchoolEventDetailViewModel(
    userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openSchoolEventEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openSchoolEventEditFragmentEvent: LiveData<Event<Unit>> = _openSchoolEventEditFragmentEvent

    private val _openDialogDeleteEvent = MutableLiveData<Event<Unit>>()
    val openDialogDeleteEvent: LiveData<Event<Unit>> = _openDialogDeleteEvent

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    val user = userRepository.currentUser.value!!
    val schoolEvent = schoolEventRepository.currentEvent.value!!

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun deleteSchoolEvent() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (schoolEventRepository.deleteSchoolEvent(user)) {
                is Result.Success -> {
                    when (chatRepository.deleteChat(
                        user,
                        schoolEvent)) {
                        is Result.Success -> _dataLoading.value = false
                        is Result.Error ->
                            _showSnackbar.value = "Ошибка при удалении чата"
                        is Result.Error ->
                            _showSnackbar.value = "Ошибка при удалении чата"
                    }
                }

                is Result.Canceled ->
                    _showSnackbar.value = "Ошибка при удалении мероприятия. Попробуйте снова"

                is Result.Error ->
                    _showSnackbar.value = "Ошибка при удалении мероприятия. Попробуйте снова"
            }
        }
    }

    fun openSchoolEventEditFragment() {
        if (user.id == schoolEvent.author.id) {
            _openSchoolEventEditFragmentEvent.value = Event(Unit)
            schoolEventRepository.stateEvent.value = State.EDIT
        } else {
            _showSnackbar.value = "Право редактирование предоставлено только автору объявления"
        }

    }

    fun showDialogDeleteSchoolEvent() {
        if (user.name == schoolEvent.author.name)  {
            _openDialogDeleteEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = "Право удаления предоставлено только автору объявления"
        }
    }
}
