package com.bogiruapps.rdshapp.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class ChatsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _showChatsContent = MutableLiveData<Event<Unit>>()
    val showChatsContent: LiveData<Event<Unit>> = _showChatsContent

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryChats()) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                    showChatsContent()
                }
            }
        }
    }

    private fun showChatsContent() {
        _showChatsContent.value = Event(Unit)
    }

}