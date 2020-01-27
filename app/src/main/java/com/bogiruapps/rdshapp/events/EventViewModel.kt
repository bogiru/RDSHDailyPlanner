package com.bogiruapps.rdshapp.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.User
import com.bogiruapps.rdshapp.UserRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openTaskEventFragment = MutableLiveData<Event<Unit>>()
    val openTaskEventFragment: LiveData<Event<Unit>> = _openTaskEventFragment

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<SchoolEvent> {
        return userRepository.fetchFirestoreRecyclerOptionsEvents()
    }

    fun showTaskEventFragment(event: SchoolEvent) {
        userRepository.currentEvent.value = event
        _openTaskEventFragment.value = Event(Unit)
    }
}