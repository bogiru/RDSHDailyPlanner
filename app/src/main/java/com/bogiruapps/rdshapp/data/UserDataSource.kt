package com.bogiruapps.rdshapp.data

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User


interface UserDataSource {

    suspend fun createUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchUsers(school: School): Result<List<User>>

    suspend fun fetchSchools(): Result<List<School>>

    suspend fun addUserToSchool(school: School, user: User): Result<Void?>

    suspend fun deleteUserFromSchool(school: School, user: User): Result<Void?>

    suspend fun createNotice(school: School, notice: Notice): Result<Void>

    suspend fun updateNotice(school: School, notice: Notice): Result<Void?>

    suspend fun deleteNotice(school: School, notice: Notice): Result<Void?>

    suspend fun createEvent(school: School, event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(school: School, event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(school: School, event: SchoolEvent): Result<Void?>

    suspend fun createChat(school: School, chat: Chat): Result<Void?>

    suspend fun createTaskEvent(school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun updateTaskEvent(school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun deleteTaskEvent(school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

}