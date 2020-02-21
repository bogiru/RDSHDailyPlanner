package com.bogiruapps.rdshapp.data
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.State
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

interface UserRepository {

    val currentUser: MutableLiveData<User>
    val currentNotice: MutableLiveData<Notice>
    val currentEvent: MutableLiveData<SchoolEvent>
    val stateEvent: MutableLiveData<State>
    val stateNotice: MutableLiveData<State>

    suspend fun createNewUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchSchools(): Result<List<School>>

    suspend fun addUserToSchool(): Result<Void?>

    suspend fun fetchUsers(): Result<List<User?>>

    suspend fun fetchFirestoreRecyclerQueryNotice(): Result<Query>

    suspend fun fetchFirestoreRecyclerQueryEvents(): Result<Query>

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