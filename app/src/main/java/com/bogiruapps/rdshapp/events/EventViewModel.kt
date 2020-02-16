package com.bogiruapps.rdshapp.events

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class EventViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openTaskEventFragment = MutableLiveData<Event<View>>()
    val openTaskEventFragment: LiveData<Event<View>> = _openTaskEventFragment

    private val _openEditEventFragment = MutableLiveData<Event<Unit>>()
    val openEditEventFragment: LiveData<Event<Unit>> = _openEditEventFragment

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<SchoolEvent> {
        return userRepository.fetchFirestoreRecyclerOptionsEvents()
    }

    fun showTaskEventFragment(event: SchoolEvent, v: View) {

        userRepository.currentEvent.value = event
        _openTaskEventFragment.value = Event(v)
    }

    fun showEditEventFragment() {
        userRepository.currentEvent.value = SchoolEvent()
        _openEditEventFragment.value = Event(Unit)
    }

}