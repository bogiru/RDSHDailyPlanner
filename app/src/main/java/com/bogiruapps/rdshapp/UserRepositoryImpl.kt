package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) : UserRepository {

    companion object {
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(dataSource: UserRemoteDataSource): UserRepositoryImpl {
            return if (INSTANCE == null) {
                UserRepositoryImpl(dataSource)
            } else {
                INSTANCE as UserRepositoryImpl
            }
        }
    }

    override val currentUser = MutableLiveData<User>()

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
}