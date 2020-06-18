package com.bogiruapps.rdshapp.data.user

import android.net.Uri
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRemoteDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserDataSource {

    private val ioDispatcher = Dispatchers.IO

    private val userCollection = db.collection(USERS_COLLECTION_NAME)

    override suspend fun createUser(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext try {
            userCollection.document(user.id).set(user).await()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext userCollection.document(user.id)
            .update (
            FIELD_NAME, user.name,
            FIELD_REGION, user.region,
            FIELD_CITY, user.city,
            FIELD_SCHOOL, user.school,
            FIELD_SCORE, user.score,
            FIELD_ID, user.id
        )
            .await()
    }

    override suspend fun fetchUser(userId: String): Result<User?> = withContext(ioDispatcher) {
        return@withContext try {
            when (val resultDocumentSnapshot =
                userCollection.document(userId).get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toUser())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchUsers(user: User): Result<List<User>> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result =
                db.collection(USERS_COLLECTION_NAME)
                    .whereEqualTo(FIELD_REGION, user.region)
                    .whereEqualTo(FIELD_CITY, user.city)
                    .whereEqualTo(FIELD_SCHOOL, user.school)
                    .get().await()) {
                is Result.Success -> Result.Success(result.data.toUserList())
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Canceled(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchFirestoreRecyclerQueryUser(user: User): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result =
                db.collection(USERS_COLLECTION_NAME)
                    .whereEqualTo(FIELD_REGION, user.region)
                    .whereEqualTo(FIELD_CITY, user.city)
                    .whereEqualTo(FIELD_SCHOOL, user.school)
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

    suspend fun uploadPictureToStorage(userId: String, internalUri: Uri) =
        withContext(ioDispatcher) {
            try {
                return@withContext getReferenceStorage(userId).putFile(internalUri).await()
            }catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    private fun getReferenceStorage(userId: String) =
        storage.reference.child("$USER_PICTURE_REFERENCE$userId")

}
