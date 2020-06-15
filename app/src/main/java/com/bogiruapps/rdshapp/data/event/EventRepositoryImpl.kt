package com.bogiruapps.rdshapp.data.event

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.schoolEvents.SchoolEvent
import com.bogiruapps.rdshapp.schoolEvents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class EventRepositoryImpl(
    private val dataSource: EventRemoteDataSource): EventRepository {

    override val currentEvent = MutableLiveData<SchoolEvent>()
    override val stateEvent = MutableLiveData<State>()

    override suspend fun fetchEvent(user: User, eventId: String): Result<SchoolEvent?> = coroutineScope {
        val task = async { dataSource.fetchEvent(
            user.region,
            user.city,
            user.school,
            eventId
        )}
        return@coroutineScope task.await()
    }

    override suspend fun createEvent(user: User, event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createEvent(
            user.region,
            user.city,
            user.school,
            event
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateEvent(user: User, event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateEvent(
            user.region,
            user.city,
            user.school,
            event
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteEvent(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteEvent(
            user.region,
            user.city,
            user.school,
            currentEvent.value!!
        )}
        return@coroutineScope task.await()
    }

    override suspend fun createTaskEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createTaskEvent(
            user.region,
            user.city,
            user.school,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateTaskEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateTaskEvent(
            user.region,
            user.city,
            user.school,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteTaskEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteTaskEvent(
            user.region,
            user.city,
            user.school,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope task.await()
    }

    override suspend fun fetchFirestoreRecyclerQuerySchoolEvents(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryEvent(
            user.region,
            user.city,
            user.school
        )}
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryTasksEvent(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryTaskEvent(
            user.region,
            user.city,
            user.school,
            currentEvent.value!!
        )}
        return@coroutineScope (task.await())
    }

}