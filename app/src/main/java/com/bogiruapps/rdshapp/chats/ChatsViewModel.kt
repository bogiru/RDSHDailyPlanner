package com.bogiruapps.rdshapp.chats

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

class ChatsViewModel(
    private val application: Application,
    userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openChatRoomFragment = MutableLiveData<Event<Unit>>()
    val openChatRoomEventFragment: LiveData<Event<Unit>> = _openChatRoomFragment

    private val _queryChats = MutableLiveData<Query>()
    val queryChats: LiveData<Query> = _queryChats

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val user = userRepository.currentUser.value!!

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = chatRepository.fetchFirestoreRecyclerQueryChats(user)) {
                is Result.Success -> {
                    _queryChats.value = result.data
                    _dataLoading.value = false
                }

                is Result.Canceled -> _showSnackbar.value = application.resources
                    .getString( R.string.error_fetch_chats_list)

                is Result.Error -> _showSnackbar.value = application.resources
                    .getString( R.string.error_fetch_chats_list)
            }
        }
    }

    fun openChatRoomEvent(chat: Chat) {
        chatRepository.currentChat.value = chat
        _openChatRoomFragment.value = Event(Unit)
    }

}