package com.bogiruapps.rdshapp.data

import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEvent
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRemoteDataSource(val db: FirebaseFirestore) : UserDataSource {
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
            FIELD_SCHOOL, user.school
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

    override suspend fun fetchUsers(school: School): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result =
                schoolsCollection.document(school.id).collection(USERS_COLLECTION_NAME).get().await()) {
                is Result.Success -> Result.Success(result.data.toUserList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchSchools(): Result<List<School>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = schoolsCollection.get().await()) {
                is Result.Success -> Result.Success(result.data.toSchoolList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addUserToSchool(school: School, user: User): Result<Void?> = withContext(ioDispatcher) {
            return@withContext schoolsCollection.document(school.id).collection(USERS_COLLECTION_NAME)
                .document(user.email.toString()).set(user).await()
        }

    override suspend fun createNotice(school: School, notice: Notice): Result<Void> = withContext(ioDispatcher) {
            return@withContext try {
                schoolsCollection.document(school.id).collection(NOTICE_COLLECTION_NAME).document(notice.id).set(notice)
                    .await()
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun updateNotice(school: School, notice: Notice): Result<Void?> = withContext(ioDispatcher) {
            return@withContext db.collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(NOTICE_COLLECTION_NAME).document(notice.id).update(
                    FIELD_TEXT, notice.text,
                    FIELD_TITLE, notice.title
                )
                .await()
        }

    override suspend fun deleteNotice(school: School, notice: Notice): Result<Void?> = withContext(ioDispatcher) {
            return@withContext db.collection(SCHOOL_COLLECTION_NAME)
                .document(school.id).collection(NOTICE_COLLECTION_NAME)
                .document(notice.id).delete()
                .await()

        }

    override suspend fun createEvent(school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext schoolsCollection.document(school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id).set(event).await()
    }

    override suspend fun updateEvent(school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
            return@withContext db.collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(event.id)
                .update(
                    FIELD_DESCRIPTION, event.description,
                    FIELD_TITLE, event.title,
                    FIELD_COUNT_COMPLETED_TASK, event.countCompletedTask,
                    FIELD_COUNT_TASK, event.countTask,
                    FIELD_DEADLINE, event.deadline
                )
                .await()
        }

    override suspend fun deleteEvent(school: School, event: SchoolEvent): Result<Void?> = withContext(ioDispatcher) {
            return@withContext db.collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(event.id).delete().await()
        }

    override suspend fun createTaskEvent(school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext schoolsCollection.document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskEvent.id).set(taskEvent).await()
    }

    override suspend fun updateTaskEvent(school: School, event: SchoolEvent, taskEvent: TaskEvent): Result<Void?> = withContext(ioDispatcher) {
        return@withContext schoolsCollection.document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(event.id).collection(TASKS_COLLECTION_NAME).document(taskEvent.id).update(
            FIELD_TITLE, taskEvent.title,
            FIELD_DESCRIPTION, taskEvent.description,
            FIELD_USER, taskEvent.user,
            FIELD_COMPLETED, taskEvent.completed
        ).await()
    }


    suspend fun fetchFirestoreRecyclerQueryNotice(school: School): Result<Query> = withContext(ioDispatcher) {
            return@withContext try {
                when (val result = schoolsCollection.document(school.id)
                    .collection(NOTICE_COLLECTION_NAME)
                    .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
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

    suspend fun fetchFirestoreRecyclerQueryEvent(school: School): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = schoolsCollection.document(school.id).collection(EVENTS_COLLECTION_NAME)
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


    fun fetchFirestoreRecyclerOptionsTasksEvent(school: School, event: SchoolEvent): FirestoreRecyclerOptions<TaskEvent> {
        val collection = schoolsCollection
            .document(school.id)
            .collection(EVENTS_COLLECTION_NAME)
            .document(event.id)
            .collection(TASKS_COLLECTION_NAME)
        val query = collection.orderBy(FIELD_COMPLETED, Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<TaskEvent>().setQuery(query, TaskEvent::class.java)
            .build()
    }
}

