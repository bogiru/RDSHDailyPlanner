package com.bogiruapps.rdshapp
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.firestore.FirestoreRecyclerOptions

interface UserRepository {

    val currentUser: MutableLiveData<User>
    val currentNotice: MutableLiveData<Notice>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchSchools(): Result<List<School>>

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice>

   /* suspend fun fetchNotices(school: School): Result<List<Notice>>*/

    suspend fun createNewNotice(notice: Notice): Result<Void?>

    suspend fun updateNotice(notice: Notice): Result<Void?>

    suspend fun deleteNotice(notice: Notice): Result<Void?>


}