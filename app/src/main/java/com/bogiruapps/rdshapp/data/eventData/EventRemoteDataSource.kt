package com.bogiruapps.rdshapp.data.eventData

import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRemoteDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : EventDataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun fetchEvent(region: Region, city: City, school: School, eventId: String): Result<SchoolEvent?> = withContext(ioDispatcher) {
        return@withContext try {
            when (val resultDocumentSnapshot = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(eventId).get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toEvent())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).set(event).await()
    }

    override suspend fun updateEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(
                SCHOOL_COLLECTION_NAME
            )
            .document(school.id).collection(EVENTS_COLLECTION_NAME).document(event.id)
            .update(
                FIELD_DESCRIPTION, event.description,
                FIELD_TITLE, event.title,
                FIELD_COUNT_COMPLETED_TASK, event.countCompletedTask,
                FIELD_COUNT_TASK, event.countTask,
                FIELD_DEADLINE, event.deadline,
                FIELD_INDEX_IMAGE, event.indexImage
            )
            .await()
    }

    override suspend fun deleteEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).delete().await()
    }

    override suspend fun createTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskEvent.id).set(taskEvent).await()
    }

    override suspend fun updateTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskEvent.id).update(
                FIELD_TITLE, taskEvent.title,
                FIELD_DESCRIPTION, taskEvent.description,
                FIELD_USER, taskEvent.user,
                FIELD_COMPLETED, taskEvent.completed
            ).await()
    }

    override suspend fun deleteTaskEvent(region: Region, city: City, school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).collection(TASKS_COLLECTION_NAME).document(taskEvent.id).delete().await()
    }

    suspend fun fetchFirestoreRecyclerQueryEvent(region: Region, city: City, school: School): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result =db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(school.id).collection(EVENTS_COLLECTION_NAME)
                .orderBy(FIELD_COUNT_TASK, Query.Direction.DESCENDING).orderBy(FIELD_COUNT_COMPLETED_TASK, Query.Direction.ASCENDING)
                .get().await()) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchFirestoreRecyclerQueryTaskEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(EVENTS_COLLECTION_NAME).document(event.id).collection(TASKS_COLLECTION_NAME)
                .orderBy(FIELD_COMPLETED, Query.Direction.ASCENDING).get().await()) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}