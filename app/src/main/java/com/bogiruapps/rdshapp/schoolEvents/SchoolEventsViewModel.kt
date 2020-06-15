package com.bogiruapps.rdshapp.schoolEvents

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class SchoolEventsViewModel(
    private val userRepository: UserRepository,
    private val schoolEventRepository: EventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openSchoolEventDetailFragment = MutableLiveData<Event<View>>()
    val openSchoolEventDetailFragment: LiveData<Event<View>> = _openSchoolEventDetailFragment

    private val _openSchoolEventEditFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventEditFragment: LiveData<Event<Unit>> = _openSchoolEventEditFragment

    private val _showSchoolEventContent = MutableLiveData<Event<Unit>>()
    val showSchoolEventContent: LiveData<Event<Unit>> = _showSchoolEventContent

    private val _querySchoolEvents = MutableLiveData<Query>()
    val querySchoolEvents: LiveData<Query> = _querySchoolEvents

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun fetchFirestoreRecyclerQuerySchoolEvents() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = schoolEventRepository
                .fetchFirestoreRecyclerQuerySchoolEvents(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    _querySchoolEvents.value = result.data
                    _dataLoading.value = false
                    showSchoolEventContent()
                }
            }
        }
    }

    fun showDetailSchoolEventFragment(schoolEvent: SchoolEvent, view: View) {
        schoolEventRepository.currentEvent.value = schoolEvent

        viewModelScope.launch {
            when (val result = chatRepository
                .fetchChat(userRepository.currentUser.value!!, schoolEvent.id)) {
                is Result.Success -> {
                    chatRepository.currentChat.value = result.data
                    _openSchoolEventDetailFragment.value = Event(view)
                }
            }
        }
    }

    fun showCreateSchoolEventFragment() {
        schoolEventRepository.stateEvent.value = State.CREATE
        schoolEventRepository.currentEvent.value = SchoolEvent()
        _openSchoolEventEditFragment.value = Event(Unit)
    }

    private fun showSchoolEventContent() {
        _showSchoolEventContent.value = Event(Unit)
    }


}