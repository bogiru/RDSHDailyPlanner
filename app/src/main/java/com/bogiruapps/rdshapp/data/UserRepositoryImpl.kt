package com.bogiruapps.rdshapp.data
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.State
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) :
    UserRepository {
    override val currentUser = MutableLiveData<User>()
    override val currentNotice = MutableLiveData<Notice>()
    override val currentEvent = MutableLiveData<SchoolEvent>()
    override val stateEvent = MutableLiveData<State>()
    override val stateNotice = MutableLiveData<State>()

    override suspend fun createNewUser(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.createUser(user) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateUser(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateUser(user) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun fetchUser(userId: String): Result<User?> = coroutineScope {
        val task = async { dataSource.fetchUser(userId) }
        return@coroutineScope task.await()
    }

    override suspend fun updateUserInSchool(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateUserInSchool(user) }
        return@coroutineScope task.await()
    }

    override suspend fun fetchSchools(): Result<List<School>> = coroutineScope {
        val task = async { dataSource.fetchSchools() }
        return@coroutineScope task.await()
    }

    override suspend fun addUserToSchool(): Result<Void?> = coroutineScope {
        val task = async { dataSource.addUserToSchool(currentUser.value!!.school, currentUser.value!!)}
        return@coroutineScope task.await()
    }

    override suspend fun fetchUsers(): Result<List<User>> = coroutineScope {
        val task = async { dataSource.fetchUsers(currentUser.value!!.school)}
        return@coroutineScope task.await()
    }

    override suspend fun fetchFirestoreRecyclerQueryUser(): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryUser(currentUser.value!!.school) }
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryEvents(): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryEvent(currentUser.value!!.school) }
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryNotice(): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryNotice(currentUser.value!!.school) }
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryTasksEvent(): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryTaskEvent(currentUser.value!!.school, currentEvent.value!!) }
        return@coroutineScope (task.await())
    }

    override suspend fun createEvent(event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createEvent(currentUser.value!!.school,  event) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateEvent(event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateEvent(currentUser.value!!.school, event) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteEvent(): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteEvent(currentUser.value!!.school, currentEvent.value!!)}
        return@coroutineScope task.await()
    }

    override suspend fun createTaskEvent(taskEvent: TaskEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.createTaskEvent(currentUser.value!!.school,  currentEvent.value!!, taskEvent) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateTaskEvent(taskEvent: TaskEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateTaskEvent(currentUser.value!!.school,  currentEvent.value!!, taskEvent) }
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteTaskEvent(taskEvent: TaskEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteTaskEvent(currentUser.value!!.school, currentEvent.value!!, taskEvent)}
        return@coroutineScope task.await()
    }

    override suspend fun createNewNotice(notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.createNotice(currentUser.value!!.school, notice)}
        return@coroutineScope task.await()
    }

    override suspend fun updateNotice(notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateNotice(currentUser.value!!.school, notice)}
        return@coroutineScope task.await()
    }

    override suspend fun deleteNotice(): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteNotice(currentUser.value!!.school, currentNotice.value!!)}
        return@coroutineScope task.await()
    }
}