package com.bogiruapps.rdshapp.events

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class EventViewModel(private val userRepository: UserRepository) : ViewModel() {

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

    init {
        fetchFirestoreRecyclerQuery()
    }

    private fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryEvents()) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                    showSchoolEventContent()
                }
            }
        }
    }

    fun showDetailEventFragment(event: SchoolEvent, v: View) {

        userRepository.currentEvent.value = event
        _openDetailEventFragment.value = Event(v)
    }

    fun showCreateEventFragment() {
        userRepository.stateEvent.value = State.CREATE
        userRepository.currentEvent.value = SchoolEvent()
        _openEditEventFragment.value = Event(Unit)
    }

    private fun showSchoolEventContent() {
        _showSchoolEventContent.value = Event(Unit)
    }


}