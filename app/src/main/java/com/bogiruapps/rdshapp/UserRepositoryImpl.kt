package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) : UserRepository {
    override val currentUser = MutableLiveData<User>()
    override val currentNotice = MutableLiveData<Notice>()
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

   /* override suspend fun fetchNotices(school: School): Result<List<Notice>> = coroutineScope  {
        val task = async { dataSource.fetchNotices(school)}
        return@coroutineScope task.await()
    }*/

    override fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice> {
        return dataSource.fetchFirestoreRecyclerOptions(currentUser.value!!.school)
    }

    override suspend fun createNewNotice(notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.createNotice(currentUser.value!!.school, notice)}
        return@coroutineScope task.await()
    }

    override suspend fun updateNotice(notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateNotice(currentUser.value!!.school, notice)}
        return@coroutineScope task.await()
    }

    override suspend fun deleteNotice(notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteNotice(currentUser.value!!.school, notice)}
        return@coroutineScope task.await()
    }
}