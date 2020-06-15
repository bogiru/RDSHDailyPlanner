package com.bogiruapps.rdshapp.data.schoolEvent

import com.bogiruapps.rdshapp.schoolEvents.SchoolEvent
import com.bogiruapps.rdshapp.schoolEvents.taskevent.TaskSchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SchoolEventRemoteDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : SchoolEventDataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun fetchSchoolEvent(region: Region, city: City, school: School, eventId: String): Result<SchoolEvent?> = withContext(ioDispatcher) {
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

    override suspend fun createSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).set(event).await()
    }

    override suspend fun updateSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
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
                FIELD_INDEX_IMAGE, event.imageIndex
            )
            .await()
    }

    override suspend fun deleteSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).delete().await()
    }

    override suspend fun createTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskSchoolEvent.id).set(taskSchoolEvent).await()
    }

    override suspend fun updateTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskSchoolEvent.id).update(
                FIELD_TITLE, taskSchoolEvent.title,
                FIELD_DESCRIPTION, taskSchoolEvent.description,
                FIELD_USER, taskSchoolEvent.user,
                FIELD_COMPLETED, taskSchoolEvent.completed
            ).await()
    }

    override suspend fun deleteTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent, taskSchoolEvent: TaskSchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).collection(TASKS_COLLECTION_NAME).document(taskSchoolEvent.id).delete().await()
    }

    suspend fun fetchFirestoreRecyclerQuerySchoolEvent(region: Region, city: City, school: School): Result<Query> = withContext(ioDispatcher) {
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

    suspend fun fetchFirestoreRecyclerQueryTaskSchoolEvent(region: Region, city: City, school: School, event: SchoolEvent): Result<Query> = withContext(ioDispatcher) {
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