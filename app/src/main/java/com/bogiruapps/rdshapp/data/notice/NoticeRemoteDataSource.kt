package com.bogiruapps.rdshapp.data.notice

import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoticeRemoteDataSource(
    private val db: FirebaseFirestore) : NoticeDataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun createNotice(user: User, notice: Notice): Result<Void> = withContext(ioDispatcher) {
        return@withContext try {
            db
                .collection(REGION_COLLECTION_NAME).document(user.region.id)
                .collection(CITY_COLLECTION_NAME).document(user.city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
                .collection(NOTICE_COLLECTION_NAME).document(notice.id)
                .set(notice).await()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateNotice(user: User, notice: Notice): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(NOTICE_COLLECTION_NAME).document(notice.id)
            .update(
                FIELD_TEXT, notice.text,
                FIELD_TITLE, notice.title,
                FIELD_VIEWS, notice.listOfUsersViewed
            )
            .await()
    }

    override suspend fun deleteNotice(user: User, notice: Notice): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(NOTICE_COLLECTION_NAME).document(notice.id)
            .delete().await()

    }

    suspend fun fetchFirestoreRecyclerQueryNotice(user: User): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db
                .collection(REGION_COLLECTION_NAME).document(user.region.id)
                .collection(CITY_COLLECTION_NAME).document(user.city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
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

}