package com.bogiruapps.rdshapp
import android.net.VpnService
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.withContext

interface UserRepository {

    val currentUser: MutableLiveData<User>
    var currentNotice: MutableLiveData<Notice>
    var currentEvent: MutableLiveData<SchoolEvent>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchSchools(): Result<List<School>>

    suspend fun addUserToSchool(): Result<Void?>

    suspend fun fetchStudents(): Result<List<User?>>

    fun fetchFirestoreRecyclerOptionsNotice(): FirestoreRecyclerOptions<Notice>

    fun fetchFirestoreRecyclerOptionsEvents(): FirestoreRecyclerOptions<SchoolEvent>

    fun fetchFirestoreRecyclerOptionsTasksEvent(): FirestoreRecyclerOptions<TaskEvent>

    suspend fun createTaskEvent(event: TaskEvent): Result<Void?>

    suspend fun createEvent(event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(): Result<Void?>

    suspend fun updateTaskEvent(event: TaskEvent): Result<Void?>

   /* suspend fun fetchNotices(school: School): Result<List<Notice>>*/

    suspend fun createNewNotice(notice: Notice): Result<Void?>

    suspend fun updateNotice(notice: Notice): Result<Void?>

    suspend fun deleteNotice(): Result<Void?>


}