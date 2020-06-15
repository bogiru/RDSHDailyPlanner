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
            userCollection.document(user.id.toString()).set(user).await()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext userCollection.document(user.id.toString()).update(
            FIELD_NAME, user.name,
            FIELD_EMAIL, user.id,
            FIELD_REGION, user.region,
            FIELD_CITY, user.city,
            FIELD_SCHOOL, user.school,
            FIELD_SCORE, user.score,
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

    suspend fun fetchFirestoreRecyclerQueryUser(region: Region, city: City, school: School): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(
                    SCHOOL_COLLECTION_NAME
                )
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
