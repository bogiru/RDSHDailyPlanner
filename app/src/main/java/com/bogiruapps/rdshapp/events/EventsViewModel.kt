package com.bogiruapps.rdshapp.events

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.userData.UserRepository
import com.bogiruapps.rdshapp.data.chatData.ChatRepository
import com.bogiruapps.rdshapp.data.eventData.EventRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class EventsViewModel(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val chatRepository: ChatRepository)
    : ViewModel() {

    private val _openDetailEventFragment = MutableLiveData<Event<View>>()
    val openTaskEventFragment: LiveData<Event<View>> = _openDetailEventFragment

    private val _openEditEventFragment = MutableLiveData<Event<Unit>>()
    val openEditEventFragment: LiveData<Event<Unit>> = _openEditEventFragment

    private val _showSchoolEventContent = MutableLiveData<Event<Unit>>()
    val showSchoolEventContent: LiveData<Event<Unit>> = _showSchoolEventContent

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = eventRepository.fetchFirestoreRecyclerQueryEvents(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                    showSchoolEventContent()
                }
            }
        }
    }

    fun showDetailEventFragment(event: SchoolEvent, v: View) {
        eventRepository.currentEvent.value = event

        viewModelScope.launch {
            when (val result = chatRepository.fetchChat(userRepository.currentUser.value!!, event.id)) {
                is Result.Success -> {
                    chatRepository.currentChat.value = result.data
                    _openDetailEventFragment.value = Event(v)
                }
            }
        }
    }

    fun showCreateEventFragment() {
        eventRepository.stateEvent.value = State.CREATE
        eventRepository.currentEvent.value = SchoolEvent()
        _openEditEventFragment.value = Event(Unit)
    }

    private fun showSchoolEventContent() {
        _showSchoolEventContent.value = Event(Unit)
    }


}