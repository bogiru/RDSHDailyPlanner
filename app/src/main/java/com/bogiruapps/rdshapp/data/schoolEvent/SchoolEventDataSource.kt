package com.bogiruapps.rdshapp.data.schoolEvent

import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result

interface SchoolEventDataSource {

    suspend fun fetchSchoolEvent(user: User, userId: String): Result<SchoolEvent?>

    suspend fun createSchoolEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun updateSchoolEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun deleteSchoolEvent(user: User, event: SchoolEvent): Result<Void?>

    suspend fun createTaskSchoolEvent(
        user: User,
        event: SchoolEvent,
        taskSchoolEvent: TaskSchoolEvent
    ): Result<Void?>

    suspend fun updateTaskSchoolEvent(
        user: User,
        event: SchoolEvent,
        taskSchoolEvent: TaskSchoolEvent
    ): Result<Void?>

    suspend fun deleteTaskSchoolEvent(
        user: User,
        event: SchoolEvent,
        taskSchoolEvent: TaskSchoolEvent
    ): Result<Void?>

}