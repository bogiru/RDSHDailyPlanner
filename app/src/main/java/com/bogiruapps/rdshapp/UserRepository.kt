package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData

interface UserRepository {

    val currentUser: MutableLiveData<User>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchSchools(): Result<List<String>>
}