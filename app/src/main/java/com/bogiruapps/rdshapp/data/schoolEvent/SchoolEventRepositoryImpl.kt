package com.bogiruapps.rdshapp.data.schoolEvent

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SchoolEventRepositoryImpl(
    private val dataSource: SchoolEventRemoteDataSource): SchoolEventRepository {

    override val currentEvent = MutableLiveData<SchoolEvent>()
    override val stateEvent = MutableLiveData<State>()

    override suspend fun fetchSchoolEvent(user: User, eventId: String): Result<SchoolEvent?> = coroutineScope {
        val task = async { dataSource.fetchSchoolEvent(
            user,
            eventId
        )}
        return@coroutineScope task.await()
    }

    override suspend fun createSchoolEvent(user: User, event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createSchoolEvent(
            user,
            event
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateSchoolEvent(user: User, event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateSchoolEvent(
            user,
            event
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteSchoolEvent(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteSchoolEvent(
            user,
            currentEvent.value!!
        )}
        return@coroutineScope task.await()
    }

    override suspend fun createTaskSchoolEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createTaskSchoolEvent(
            user,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateTaskSchoolEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateTaskSchoolEvent(
            user,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteTaskSchoolEvent(user: User, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteTaskSchoolEvent(
            user,
            currentEvent.value!!, taskSchoolEvent
        )}
        return@coroutineScope task.await()
    }

    override suspend fun fetchFirestoreRecyclerQuerySchoolEvents(user: User): Result<Query> = coroutineScope {
        val task = async {
            dataSource.fetchFirestoreRecyclerQuerySchoolEvent(user)
        }
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryTasksSchoolEvent(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryTaskSchoolEvent(
            user,
            currentEvent.value!!
        )}
        return@coroutineScope (task.await())
    }

}