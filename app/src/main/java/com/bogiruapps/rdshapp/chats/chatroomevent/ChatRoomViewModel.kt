package com.bogiruapps.rdshapp.chats.chatroomevent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class ChatRoomViewModel(
    private val application: Application,
    userRepository: UserRepository,
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

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val chat = chatRepository.currentChat.value!!
    private val user = userRepository.currentUser.value!!

    fun fetchFirestoreRecyclerQueryMessages() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = chatRepository.fetchFirestoreRecyclerQueryEventMessage(user)) {
                is Result.Success -> {
                    _query.value = result.data
                    _showEventChatRoomContent.value = Event(Unit)
                    _dataLoading.value = false
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_messages_list)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_messages_list)
            }
        }
    }

    fun loadMessage(textMessage: String) {
        clearEventChatRoomEdtText()
        viewModelScope.launch {
            val message = Message(textMessage, user)
            when(chatRepository.createMessage(user, message)) {
                is Result.Success -> {
                    val tempChat = chat
                    tempChat.lastMessage = message
                    when (chatRepository.updateChat(user, tempChat)) {
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
                        .getString(R.string.error_create_message_to_db)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_create_message_to_db)
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