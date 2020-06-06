package com.bogiruapps.rdshapp.data

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User


interface UserDataSource {

    suspend fun createUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchUsers(region: Region, city: City, school: School): Result<List<User>>

    suspend fun fetchRegions(): Result<List<Region>>

    suspend fun fetchCities(regionId: String): Result<List<City>>

    suspend fun fetchSchools(regionId: String, cityId: String): Result<List<School>>

    suspend fun addUserToSchool(region: Region, city: City, school: School, user: User): Result<Void?>

    suspend fun deleteUserFromSchool(region: Region, city: City, school: School, user: User): Result<Void?>

    suspend fun fetchEvent(region: Region, city: City, school: School, userId: String): Result<SchoolEvent?>

    suspend fun createEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun createChat(region: Region, city: City, school: School, chat: Chat): Result<Void?>

    suspend fun updateChat(region: Region, city: City, school: School, chat: Chat): Result<Void?>

    suspend fun createTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun updateTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun deleteTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

}