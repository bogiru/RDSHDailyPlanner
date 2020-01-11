package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice

interface UserRepository {

    val currentUser: MutableLiveData<User>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchSchools(): Result<List<String>>

    suspend fun fetchNotices(school: String): Result<List<Notice>>

    suspend fun createNewNotice(school: String, notice: Notice): Result<Void?>

}