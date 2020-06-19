package com.bogiruapps.rdshapp.schoolevents.detailevent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class SchoolEventDetailViewModel(
    private val application: Application,
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
            schoolEventRepository.deleteSchoolEvent(user)
        }
        viewModelScope.launch {
            chatRepository.deleteChat(user)
        }
    }

    fun openSchoolEventEditFragment() {
        if (user.id == schoolEvent.author.id) {
            _openSchoolEventEditFragmentEvent.value = Event(Unit)
            schoolEventRepository.stateEvent.value = State.EDIT
        } else {
            _showSnackbar.value =  application.resources
                .getString(R.string.error_not_enough_rights_to_edit)
        }

    }

    fun showDialogDeleteSchoolEvent() {
        if (user.name == schoolEvent.author.name)  {
            _openDialogDeleteEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = application.resources
                .getString(R.string.error_not_enough_rights_to_delete)

        }
    }
}
