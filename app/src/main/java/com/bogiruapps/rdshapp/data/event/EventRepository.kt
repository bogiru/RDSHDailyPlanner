package com.bogiruapps.rdshapp.data.event

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.schoolEvents.SchoolEvent
import com.bogiruapps.rdshapp.schoolEvents.tasksEvent.SchoolTaskEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query

interface EventRepository {

    val currentEvent: MutableLiveData<SchoolEvent>
    val stateEvent: MutableLiveData<State>

    suspend fun createTaskEvent(user: User, eventSchool: SchoolTaskEvent): Result<Void?>

    suspend fun fetchEvent(user: User, eventId: String): Result<SchoolEvent?>

    suspend fun createEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(user: User): Result<Void?>

    suspend fun updateTaskEvent(user: User, eventSchool: SchoolTaskEvent): Result<Void?>

    suspend fun deleteTaskEvent(user: User, schoolTaskEvent: SchoolTaskEvent): Result<Void?>

    suspend fun fetchFirestoreRecyclerQuerySchoolEvents(user: User): Result<Query>

    suspend fun fetchFirestoreRecyclerQueryTasksEvent(user: User):  Result<Query>

}