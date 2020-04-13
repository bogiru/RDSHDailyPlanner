package com.bogiruapps.rdshapp.events.chat_room_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class EventChatRoomViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    private val _showEventChatRoomContent = MutableLiveData<Event<Unit>>()
    val showEventChatRoomContent: LiveData<Event<Unit>> = _showEventChatRoomContent

    private val _updateEventChatRoomRecyclerView = MutableLiveData<Event<Unit>>()
    val updateEventChatRoomRecyclerView: LiveData<Event<Unit>> = _updateEventChatRoomRecyclerView

    private val _clearEventChatRoomEdtText = MutableLiveData<Event<Unit>>()
    val clearEventChatRoomEdtText: LiveData<Event<Unit>> = _clearEventChatRoomEdtText

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun fetchFirestoreRecyclerQuery() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryEventMessage()) {
                is Result.Success -> {
                    _query.value = result.data
                    _showEventChatRoomContent.value = Event(Unit)
                    _dataLoading.value = false
                }
            }
        }
    }

    fun loadMessage(textMessage: String) {
        clearEventChatRoomEdtText()
        viewModelScope.launch {
            val message = Message(textMessage, userRepository.currentUser.value!!)
            when(userRepository.createEventMessage(message)) {
                is Result.Success -> {
                }
            }
        }
    }

    fun updateEventChatRoomRecyclerView() {
        _updateEventChatRoomRecyclerView.value = Event(Unit)
    }

    private fun clearEventChatRoomEdtText() {
        _clearEventChatRoomEdtText.value = Event(Unit)
    }

}