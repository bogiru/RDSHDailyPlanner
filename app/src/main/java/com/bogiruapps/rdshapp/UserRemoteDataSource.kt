package com.bogiruapps.rdshapp

import android.util.Log
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRemoteDataSource(val db: FirebaseFirestore) : UserDataSource {
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
            "school", user.school.name
        ).await()
    }

    suspend fun fetchUser(userId: String): Result<User?> = withContext(ioDispatcher) {
        return@withContext try {
            when(val resultDocumentSnapshot = userCollection.document(userId).get().await()) {
                is Result.Success -> {
                    val schoolName = resultDocumentSnapshot.data["school"].toString()
                    if (schoolName == "") Result.Success(resultDocumentSnapshot.data.toUser(""))
                    else {
                        when (val idSchool = fetchIdSchool(schoolName)) {
                            is Result.Success -> {
                                Result.Success(
                                    resultDocumentSnapshot.data.toUser(
                                        idSchool.data
                                    )
                                )

                            }
                            is Result.Error -> Result.Error(idSchool.exception)
                            is Result.Canceled -> Result.Canceled(idSchool.exception)
                        }
                    }
                }
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchSchools(): Result<List<School>> = withContext(ioDispatcher) {
        return@withContext try {
            val data = System.currentTimeMillis()
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
    suspend fun createNotice(school: School, notice: Notice): Result<Void> = withContext(ioDispatcher) {
       return@withContext try {
           schoolsCollection
               .document(school.id)
               .collection("notices")
               .document()
               .set(hashMapOf("text" to notice.text, "title" to notice.title, "date" to notice.date, "author" to notice.author))
               .await()
       } catch (e: Exception) {
           Result.Error(e)
       }
   }

    suspend fun updateNotice(school: School, notice: Notice):  Result<Void?> = withContext(ioDispatcher) {

        return@withContext db.collection("schools")
                    .document(school.id)
                    .collection("notices")
                    .document(notice.id)
                    .update(
                        "text", notice.text,
                        "title", notice.title)
                    .await()
    }

    suspend fun deleteNotice(school: School, notice: Notice):  Result<Void?> = withContext(ioDispatcher) {
        Log.i("Deletee", notice.id + " " + school.name)
        return@withContext db.collection("schools")
            .document(school.id)
            .collection("notices")
            .document(notice.id)
            .delete()
            .addOnSuccessListener {  }
            .addOnFailureListener { }
            .await()

    }

    /*suspend fun fetchNotices(school: School): Result<List<Notice>> = withContext(ioDispatcher) {

        return@withContext try {
            when (val result =
                schoolsCollection.document(school.id).collection("notices").get()
                    .await()) {
                is Result.Success -> Result.Success(result.data.toNoticeList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }*/

    fun fetchFirestoreRecyclerOptions(school: School): FirestoreRecyclerOptions<Notice> {
        val collection = schoolsCollection.document(school.id).collection("notices")
        val query = collection.orderBy("date", Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<Notice>().setQuery(query, Notice::class.java).build()
    }






}

