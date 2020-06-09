package com.bogiruapps.rdshapp.data.event

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query

interface EventRepository {

    val currentEvent: MutableLiveData<SchoolEvent>
    val stateEvent: MutableLiveData<State>

    suspend fun createTaskEvent(user: User, event: TaskEvent): Result<Void?>

    suspend fun fetchEvent(user: User, eventId: String): Result<SchoolEvent?>

    suspend fun createEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(user: User): Result<Void?>

    suspend fun updateTaskEvent(user: User, event: TaskEvent): Result<Void?>

    suspend fun deleteTaskEvent(user: User, taskEvent: TaskEvent): Result<Void?>

    suspend fun fetchFirestoreRecyclerQuerySchoolEvents(user: User): Result<Query>

    suspend fun fetchFirestoreRecyclerQueryTasksEvent(user: User):  Result<Query>

}