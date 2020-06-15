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
    private val eventRepository: EventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openDetailEventFragment = MutableLiveData<Event<View>>()
    val openTaskEventFragment: LiveData<Event<View>> = _openDetailEventFragment

    private val _openEditEventFragment = MutableLiveData<Event<Unit>>()
    val openEditEventFragment: LiveData<Event<Unit>> = _openEditEventFragment

    private val _showSchoolEventContent = MutableLiveData<Event<Unit>>()
    val showSchoolEventContent: LiveData<Event<Unit>> = _showSchoolEventContent

    private val _querySchoolEvents = MutableLiveData<Query>()
    val querySchoolEvents: LiveData<Query> = _querySchoolEvents

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun fetchFirestoreRecyclerQuerySchoolEvents() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = eventRepository
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
        eventRepository.currentEvent.value = schoolEvent

        viewModelScope.launch {
            when (val result = chatRepository
                .fetchChat(userRepository.currentUser.value!!, schoolEvent.id)) {
                is Result.Success -> {
                    chatRepository.currentChat.value = result.data
                    _openDetailEventFragment.value = Event(view)
                }
            }
        }
    }

    fun showCreateSchoolEventFragment() {
        eventRepository.stateEvent.value = State.CREATE
        eventRepository.currentEvent.value = SchoolEvent()
        _openEditEventFragment.value = Event(Unit)
    }

    private fun showSchoolEventContent() {
        _showSchoolEventContent.value = Event(Unit)
    }


}