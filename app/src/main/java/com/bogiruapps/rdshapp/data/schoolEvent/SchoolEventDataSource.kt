package com.bogiruapps.rdshapp.data.schoolEvent

import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result

interface SchoolEventDataSource {

    suspend fun fetchSchoolEvent(region: Region, city: City, school: School, userId: String): Result<SchoolEvent?>

    suspend fun createSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun updateSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun deleteSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun createTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?>

    suspend fun updateTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?>

    suspend fun deleteTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?>

}