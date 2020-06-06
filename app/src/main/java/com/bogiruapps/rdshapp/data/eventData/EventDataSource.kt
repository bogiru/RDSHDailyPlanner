package com.bogiruapps.rdshapp.data.eventData

import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result

interface EventDataSource {

    suspend fun fetchEvent(region: Region, city: City, school: School, userId: String): Result<SchoolEvent?>

    suspend fun createEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun updateEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun deleteEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?>

    suspend fun createTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun updateTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

    suspend fun deleteTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?>

}