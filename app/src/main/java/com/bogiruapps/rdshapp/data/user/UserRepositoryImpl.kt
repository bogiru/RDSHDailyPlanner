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

    override suspend fun updateUserInSchool(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateUserInSchool(user) }
        return@coroutineScope task.await()
    }

    override suspend fun fetchRegions(): Result<List<Region>> = coroutineScope {
        val task = async { dataSource.fetchRegions() }
        return@coroutineScope task.await()
    }

    override suspend fun fetchCities(): Result<List<City>> = coroutineScope {
        val task = async { dataSource.fetchCities(currentUser.value!!.region.id) }
        return@coroutineScope task.await()
    }

    override suspend fun fetchSchools(): Result<List<School>> = coroutineScope {
        val task = async { dataSource.fetchSchools(
            currentUser.value!!.region.id,
            currentUser.value!!.city.id
        ) }
        return@coroutineScope task.await()
    }

    override suspend fun addUserToSchool(): Result<Void?> = coroutineScope {
        val task = async { dataSource.addUserToSchool(
            currentUser.value!!.region,
            currentUser.value!!.city,
            currentUser.value!!.school,
            currentUser.value!!)}
        return@coroutineScope task.await()
    }

    override suspend fun deleteUserFromSchool(): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteUserFromSchool(
            currentUser.value!!.region,
            currentUser.value!!.city,
            currentUser.value!!.school,
            currentUser.value!!) }
        return@coroutineScope task.await()
    }

    override suspend fun fetchUsers(): Result<List<User>> = coroutineScope {
        val task = async { dataSource.fetchUsers(currentUser.value!!.region,
            currentUser.value!!.city,
            currentUser.value!!.school
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

    override suspend fun updateUserPicture(user: User, internalUri: Uri): Result<Uri?> {
        val uri = dataSource.createNewPictureInStorage(user.id, internalUri)

        when (uri) {
            is Result.Success -> {
                val uriPicture = uri.data.toString()
                user.pictureUrl = uriPicture
                when (updateUser(user)) {
                    is Result.Error, is Result.Canceled -> {
                        return Result.Error(Exception("Ошибка при обновлении информации пользователя"))
                    }
                }
            }
        }
        return uri
    }
}