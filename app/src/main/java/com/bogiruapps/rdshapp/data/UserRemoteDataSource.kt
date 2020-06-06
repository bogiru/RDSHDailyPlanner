package com.bogiruapps.rdshapp.data

import android.net.Uri
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.chats.chat_room_event.Message
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRemoteDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserDataSource {

    private val ioDispatcher = Dispatchers.IO

    private val userCollection = db.collection(USERS_COLLECTION_NAME)
    private val schoolsCollection = db.collection(SCHOOL_COLLECTION_NAME)

    override suspend fun createUser(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext try {
            userCollection.document(user.email.toString()).set(user).await()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext userCollection.document(user.email.toString()).update(
            FIELD_NAME, user.name,
            FIELD_EMAIL, user.email,
            FIELD_REGION, user.region,
            FIELD_CITY, user.city,
            FIELD_SCHOOL, user.school,
            FIELD_SCORE, user.score,
            FIELD_IMAGE_URL, user.pictureUrl,
            FIELD_ADMIN, user.admin,
            FIELD_ID, user.id
        ).await()
    }

    override suspend fun fetchUser(userId: String): Result<User?> = withContext(ioDispatcher) {
        return@withContext try {
            when (val resultDocumentSnapshot = userCollection.document(userId).get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toUser())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchUsers(region: Region, city: City, school: School): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result =
                db.collection(REGION_COLLECTION_NAME).document(region.id)
                    .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                    .document(school.id).collection(USERS_COLLECTION_NAME).get().await()) {
                is Result.Success -> Result.Success(result.data.toUserList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateUserInSchool(user: User) = withContext(ioDispatcher) {
        return@withContext schoolsCollection.document(user.school.id).collection(USERS_COLLECTION_NAME).document(user.email.toString()).update(
            FIELD_SCORE, user.score
        ).await()
    }

    override suspend fun fetchRegions(): Result<List<Region>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).get().await()) {
                is Result.Success -> Result.Success(result.data.toRegionList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchCities(regionId: String): Result<List<City>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(regionId)
                .collection(CITY_COLLECTION_NAME).get().await()) {
                is Result.Success -> Result.Success(result.data.toCityList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchSchools(regionId: String, cityId: String): Result<List<School>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(regionId)
                .collection(CITY_COLLECTION_NAME).document(cityId).collection(SCHOOL_COLLECTION_NAME).get().await()) {
                is Result.Success -> Result.Success(result.data.toSchoolList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addUserToSchool(region: Region, city: City, school: School, user: User): Result<Void?> = withContext(ioDispatcher) {
            return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(USERS_COLLECTION_NAME)
                .document(user.email.toString()).set(user).await()
        }

    override suspend fun deleteUserFromSchool(region: Region, city: City, school: School, user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(USERS_COLLECTION_NAME)
            .document(user.email.toString()).delete().await()
    }

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
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
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
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(EVENTS_COLLECTION_NAME).document(event.id).delete().await()
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

    override suspend fun createChat(region: Region, city: City, school: School, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(CHATS_COLLECTION_NAME).document(chat.id).set(chat).await()
    }

    override suspend fun updateChat(region: Region, city: City, school: School, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(CHATS_COLLECTION_NAME).document(chat.id)
            .update(
                FIELD_TITLE, chat.title,
                FIELD_LAST_MESSAGE, chat.lastMessage
            )
            .await()
    }

    suspend fun createEventMessage(region: Region, city: City, school: School, event: SchoolEvent, message: Message): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(MESSAGES_COLLECTION_NAME).document().set(message).await()
    }

    suspend fun fetchFirestoreRecyclerQueryUser(region: Region, city: City, school: School): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(USERS_COLLECTION_NAME)
                .orderBy(FIELD_SCORE, Query.Direction.DESCENDING)
                .get().await()
                ) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
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

    suspend fun fetchFirestoreRecyclerQueryChats(region: Region, city: City, school: School): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(CHATS_COLLECTION_NAME)
                .get().await()) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchFirestoreRecyclerQueryEventMessage(region: Region, city: City, school: School, event: SchoolEvent): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(event.id).collection(
                    MESSAGES_COLLECTION_NAME)
                .orderBy(FIELD_DATE, Query.Direction.DESCENDING).get().await()) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun createNewPictureInStorage(userId: String, internalUri: Uri): Result<Uri> {
        return try {
            uploadPictureToStorage(userId, internalUri)
            fetchPictureUriFromStorage(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun uploadPictureToStorage(userId: String, internalUri: Uri) =
        withContext(ioDispatcher) {
            getReferenceStorage(userId).putFile(internalUri).await<UploadTask.TaskSnapshot>()
        }

    private suspend fun fetchPictureUriFromStorage(userId: String): Result<Uri> =
        withContext(ioDispatcher) {
            return@withContext getReferenceStorage(userId).downloadUrl.await()
        }

    private fun getReferenceStorage(userId: String) =
        storage.reference.child("$USER_PICTURE_REFERENCE$userId")

}
