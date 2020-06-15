package com.bogiruapps.rdshapp.data.user
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.google.firebase.firestore.Query
import com.google.firebase.storage.UploadTask

interface UserRepository {

    val currentUser: MutableLiveData<User>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchUsers(): Result<List<User?>>

    suspend fun fetchFirestoreRecyclerQueryUser():  Result<Query>

    suspend fun updateUserPicture(user: User, internalUri: Uri): Result<UploadTask.TaskSnapshot>
}