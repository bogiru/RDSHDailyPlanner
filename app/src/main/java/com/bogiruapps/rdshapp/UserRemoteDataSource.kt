package com.bogiruapps.rdshapp

import android.util.Log
import com.bogiruapps.rdshapp.notice.Notice
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRemoteDataSource(db: FirebaseFirestore) : UserDataSource {

    companion object {
        private var INSTANCE: UserRemoteDataSource? = null

        fun getInstance(database: FirebaseFirestore): UserRemoteDataSource {
            if (INSTANCE == null) INSTANCE = UserRemoteDataSource(database)
            return INSTANCE as UserRemoteDataSource
        }
    }


    private val ioDispatcher =  Dispatchers.IO
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
            "name", user.name,
            "email", user.email,
            "school", user.school
        ).await()
    }

    suspend fun fetchUser(userId: String): Result<User?> = withContext(ioDispatcher) {
        return@withContext try {
            when(val resultDocumentSnapshot = userCollection.document(userId).get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toUser())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchSchools(): Result<List<String>> = withContext(ioDispatcher) {
        return@withContext try {
            when(val result = schoolsCollection.get().await()) {
                is Result.Success -> Result.Success(result.data.toSchoolList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun fetchIdSchool(school: String): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = schoolsCollection.whereEqualTo("name", school).get().await()) {
                is Result.Success -> Result.Success(result.data.documents[0].id)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
/* */
    suspend fun createNotice(schoolName: String, notice: Notice): Result<Void> = withContext(ioDispatcher) {
       return@withContext try {
           when (val idSchool = fetchIdSchool(schoolName)) {
               is Result.Success ->
                   schoolsCollection
                       .document(idSchool.data)
                       .collection("notices")
                       .document()
                       .set(hashMapOf("text" to notice.text))
                       .await()

               is Result.Error -> Result.Error(idSchool.exception)
               is Result.Canceled -> Result.Canceled(idSchool.exception)

           }
       } catch (e: Exception) {
           Result.Error(e)
       }
   }

    suspend fun fetchNotices(schoolName: String): Result<List<Notice>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val idSchool = fetchIdSchool(schoolName)) {
                is Result.Success -> {
                    when (val result =
                        schoolsCollection.document(idSchool.data).collection("notices").get()
                            .await()) {
                        is Result.Success -> Result.Success(result.data.toNoticeList())
                        is Result.Error -> Result.Error(result.exception)
                        is Result.Canceled -> Result.Canceled(result.exception)
                    }
                }
                is Result.Error -> Result.Error(idSchool.exception)
                is Result.Canceled -> Result.Canceled(idSchool.exception)

            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}

