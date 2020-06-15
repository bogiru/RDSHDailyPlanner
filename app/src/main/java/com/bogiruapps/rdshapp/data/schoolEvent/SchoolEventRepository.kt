package com.bogiruapps.rdshapp.data.schoolEvent

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query

interface SchoolEventRepository {

    val currentEvent: MutableLiveData<SchoolEvent>
    val stateEvent: MutableLiveData<State>

    suspend fun createTaskSchoolEvent(user: User, eventSchool: TaskSchoolEvent): Result<Void?>

    suspend fun fetchSchoolEvent(user: User, eventId: String): Result<SchoolEvent?>

    suspend fun createSchoolEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun updateSchoolEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun deleteSchoolEvent(user: User): Result<Void?>

    suspend fun updateTaskSchoolEvent(user: User, eventSchool: TaskSchoolEvent): Result<Void?>

    suspend fun deleteTaskSchoolEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?>

    suspend fun fetchFirestoreRecyclerQuerySchoolEvents(user: User): Result<Query>

    suspend fun fetchFirestoreRecyclerQueryTasksSchoolEvent(user: User):  Result<Query>

}