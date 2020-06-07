package com.bogiruapps.rdshapp.data.chat

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chat_room_event.Message
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRemoteDataSource(private val db: FirebaseFirestore) : ChatDataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun fetchChat(region: Region, city: City, school: School, chatId: String): Result<Chat?> = withContext(ioDispatcher) {
        return@withContext try {
            when (val resultDocumentSnapshot = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(chatId).get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toChat())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
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

    override suspend fun deleteChat(region: Region, city: City, school: School, event: SchoolEvent, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME).document(event.id)
            .collection(CHATS_COLLECTION_NAME).document(chat.id).delete().await()
    }

    override suspend fun createMessage(region: Region, city: City, school: School, chatId: String, message: Message): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db.collection(REGION_COLLECTION_NAME).document(region.id)
            .collection(CITY_COLLECTION_NAME).document(city.id).collection(SCHOOL_COLLECTION_NAME)
            .document(school.id).collection(EVENTS_COLLECTION_NAME)
            .document(chatId).collection(MESSAGES_COLLECTION_NAME).document().set(message).await()
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

    suspend fun fetchFirestoreRecyclerQueryMessage(region: Region, city: City, school: School, chatId: String): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db.collection(REGION_COLLECTION_NAME).document(region.id)
                .collection(CITY_COLLECTION_NAME).document(city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(school.id)
                .collection(EVENTS_COLLECTION_NAME).document(chatId).collection(
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

}