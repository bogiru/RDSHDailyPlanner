package com.bogiruapps.rdshapp.chats.chatroomevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class ChatRoomViewModel(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository)
    : ViewModel() {

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
            when (val result = chatRepository.fetchFirestoreRecyclerQueryEventMessage(userRepository.currentUser.value!!)) {
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
            when(chatRepository.createMessage(userRepository.currentUser.value!!, message)) {
                is Result.Success -> {
                    val chat = Chat(chatRepository.currentChat.value!!.id,
                        chatRepository.currentChat.value!!.title,
                        message,
                        chatRepository.currentChat.value!!.indexImage)
                    when (chatRepository.updateChat(userRepository.currentUser.value!!, chat)) {
                        is Result.Success -> {}
                    }
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