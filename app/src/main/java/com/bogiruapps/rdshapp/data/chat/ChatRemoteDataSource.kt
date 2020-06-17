package com.bogiruapps.rdshapp.data.chat

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRemoteDataSource(private val db: FirebaseFirestore) : ChatDataSource {

    private val ioDispatcher = Dispatchers.IO

    override suspend fun fetchChat(user: User, chatId: String): Result<Chat?> = withContext(ioDispatcher) {
        return@withContext try {
            when (val resultDocumentSnapshot = db
                .collection(REGION_COLLECTION_NAME).document(user.region.id)
                .collection(CITY_COLLECTION_NAME).document(user.city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
                .collection(EVENTS_COLLECTION_NAME).document(chatId)
                .get().await()) {
                is Result.Success -> Result.Success(resultDocumentSnapshot.data.toChat())
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(resultDocumentSnapshot.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createChat(user: User, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(CHATS_COLLECTION_NAME).document(chat.id)
            .set(chat).await()
    }

    override suspend fun updateChat(user: User, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(CHATS_COLLECTION_NAME).document(chat.id)
            .update(
                FIELD_TITLE, chat.title,
                FIELD_LAST_MESSAGE, chat.lastMessage,
                FIELD_IMAGE_INDEX, chat.imageIndex
            )
            .await()
    }

    override suspend fun deleteChat(user: User, event: SchoolEvent, chat: Chat): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(EVENTS_COLLECTION_NAME).document(event.id)
            .collection(CHATS_COLLECTION_NAME).document(chat.id)
            .delete().await()
    }

    override suspend fun createMessage(user: User, chatId: String, message: Message): Result<Void?> = withContext(ioDispatcher) {
        return@withContext db
            .collection(REGION_COLLECTION_NAME).document(user.region.id)
            .collection(CITY_COLLECTION_NAME).document(user.city.id)
            .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
            .collection(EVENTS_COLLECTION_NAME).document(chatId)
            .collection(MESSAGES_COLLECTION_NAME).document()
            .set(message).await()
    }

    suspend fun fetchFirestoreRecyclerQueryChats(user: User): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db
                .collection(REGION_COLLECTION_NAME).document(user.region.id)
                .collection(CITY_COLLECTION_NAME).document(user.city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
                .collection(CHATS_COLLECTION_NAME)
                .get().await()) {
                is Result.Success -> Result.Success(result.data.query)
                is Result.Error -> Result.Error(result.exception)
                is Result.Canceled -> Result.Error(result.exception)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun fetchFirestoreRecyclerQueryMessage(user: User, chatId: String): Result<Query> = withContext(ioDispatcher) {
        return@withContext try {
            when (val result = db
                .collection(REGION_COLLECTION_NAME).document(user.region.id)
                .collection(CITY_COLLECTION_NAME).document(user.city.id)
                .collection(SCHOOL_COLLECTION_NAME).document(user.school.id)
                .collection(EVENTS_COLLECTION_NAME).document(chatId)
                .collection(MESSAGES_COLLECTION_NAME)
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