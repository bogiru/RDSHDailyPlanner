package com.bogiruapps.rdshapp.events.tasksEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TaskEventViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<TaskEvent> {
        Log.i("QWE", "rex ${userRepository.currentEvent.value}")
        return userRepository.fetchFirestoreRecyclerOptionsTasksEvent()
    }
}