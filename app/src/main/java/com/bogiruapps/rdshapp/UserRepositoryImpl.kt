package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) : UserRepository {

    companion object {
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(dataSource: UserRemoteDataSource): UserRepositoryImpl {
            if (INSTANCE == null) INSTANCE = UserRepositoryImpl(dataSource)
            return INSTANCE as UserRepositoryImpl
        }
    }

    override val currentUser = MutableLiveData<User>()
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

    override suspend fun fetchSchools(): Result<List<String>> = coroutineScope {
        val task = async { dataSource.fetchSchools() }
        return@coroutineScope task.await()
    }

    override suspend fun fetchNotices(schoolName: String): Result<List<Notice>> = coroutineScope  {
        val task = async { dataSource.fetchNotices(schoolName)}
        return@coroutineScope task.await()
    }

    override suspend fun createNewNotice(schoolName: String, notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.createNotice(schoolName, notice)}
        return@coroutineScope task.await()
    }
}