package com.bogiruapps.rdshapp.data.chat

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.returnSuccessOrError
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ChatRepositoryImpl(private val dataSource: ChatRemoteDataSource): ChatRepository {

    override val currentChat = MutableLiveData<Chat>()

    override suspend fun fetchFirestoreRecyclerQueryChats(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryChats(user)}
        return@coroutineScope (task.await())
    }

    override suspend fun fetchFirestoreRecyclerQueryEventMessage(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryMessage(
                user,
                currentChat.value!!.id
            )}
        return@coroutineScope (task.await())
    }

    override suspend fun fetchChat(user: User, chatId: String): Result<Chat?> = coroutineScope {
        val task = async { dataSource.fetchChat(
            user,
            chatId)}

        return@coroutineScope task.await()
    }

    override suspend fun createChat(user: User, chat: Chat): Result<Void?> = coroutineScope {
        val task = async { dataSource.createChat(
            user,
            chat
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun updateChat(user: User, chat: Chat): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateChat(
            user,
            chat
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

    override suspend fun deleteChat(user: User, event: SchoolEvent): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteChat(
            user,
            event,
            currentChat.value!!
        )}
        return@coroutineScope task.await()
    }

    override suspend fun createMessage(user: User, message: Message): Result<Void?> = coroutineScope {
        val task = async { dataSource.createMessage(
            user,
            currentChat.value!!.id, message
        )}
        return@coroutineScope returnSuccessOrError(task.await())
    }

}