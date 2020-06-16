package com.bogiruapps.rdshapp.data.user
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.bogiruapps.rdshapp.school.School
import com.google.firebase.firestore.Query
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


class UserRepositoryImpl(private val dataSource: UserRemoteDataSource) :
    UserRepository {
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

    override suspend fun fetchUsers(): Result<List<User>> = coroutineScope {
        val task = async {
            dataSource.fetchUsers(currentUser.value!!
            )}
        return@coroutineScope task.await()
    }

    override suspend fun fetchFirestoreRecyclerQueryUser(): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryUser(
            currentUser.value!!.region,
            currentUser.value!!.city,
            currentUser.value!!.school
        )}
        return@coroutineScope (task.await())
    }

    override suspend fun updateUserPicture(user: User, internalUri: Uri): Result<UploadTask.TaskSnapshot> = coroutineScope {
        val task = async { dataSource.uploadPictureToStorage(user.id, internalUri) }
        return@coroutineScope task.await()
    }
}