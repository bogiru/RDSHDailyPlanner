package com.bogiruapps.rdshapp.data
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) :
    UserRepository {
    override val currentUser = MutableLiveData<User>()
    override var currentNotice = MutableLiveData<Notice>()
    override var currentEvent = MutableLiveData<SchoolEvent>()
/*    override val schoolsUser = MutableLiveData<String>()*/


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

    override suspend fun fetchSchools(): Result<List<School>> = coroutineScope {
        val task = async { dataSource.fetchSchools() }
        return@coroutineScope task.await()
    }

    override suspend fun addUserToSchool(): Result<Void?> = coroutineScope {
        val task = async { dataSource.addUserToSchool(currentUser.value!!.school, currentUser.value!!)}
        return@coroutineScope task.await()
    }

    override suspend fun fetchStudents(): Result<List<User>> = coroutineScope {
        val task = async { dataSource.fetchUsers(currentUser.value!!.school)}
        return@coroutineScope task.await()
    }


    /* override suspend fun fetchNotices(school: School): Result<List<Notice>> = coroutineScope  {
         val task = async { dataSource.fetchNotices(school)}
         return@coroutineScope task.await()
     }*/

    override fun fetchFirestoreRecyclerOptionsEvents(): FirestoreRecyclerOptions<SchoolEvent> {
        return dataSource.fetchFirestoreRecyclerOptionsEvent(currentUser.value!!.school)
    }

    override fun fetchFirestoreRecyclerOptionsNotice(): FirestoreRecyclerOptions<Notice> {
        return dataSource.fetchFirestoreRecyclerOptionsNotice(currentUser.value!!.school)
    }

    override fun fetchFirestoreRecyclerOptionsTasksEvent(): FirestoreRecyclerOptions<TaskEvent> {
        return dataSource.fetchFirestoreRecyclerOptionsTasksEvent(currentUser.value!!.school, currentEvent.value!!)
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