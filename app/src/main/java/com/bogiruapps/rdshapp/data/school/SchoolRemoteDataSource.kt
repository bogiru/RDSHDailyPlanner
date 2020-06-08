package com.bogiruapps.rdshapp.data.school

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SchoolRemoteDataSource(private val db: FirebaseFirestore): SchoolDataSource {

    private val ioDispatcher = Dispatchers.IO

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

    override suspend fun addUserToSchool(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id).collection(
                SCHOOL_COLLECTION_NAME
            )
            .document(user.school.id).collection(USERS_COLLECTION_NAME)
            .document(user.email.toString()).set(user).await()
    }

    override suspend fun deleteUserFromSchool(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(user.school.id).collection(USERS_COLLECTION_NAME)
            .document(user.email.toString()).delete().await()
    }

    override suspend fun updateUserInSchool(user: User): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(SCHOOL_COLLECTION_NAME).document(user.school.id).collection(
            USERS_COLLECTION_NAME
        ).document(user.email.toString()).update(
            FIELD_SCORE, user.score
        ).await()
    }
}