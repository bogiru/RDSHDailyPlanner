package com.bogiruapps.rdshapp.data.chatData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chat_room_event.Message
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query

interface ChatRepository {

    val currentChat: MutableLiveData<Chat>

    suspend fun fetchFirestoreRecyclerQueryChats(user: User): Result<Query>

    suspend fun fetchFirestoreRecyclerQueryEventMessage(user: User): Result<Query>

    suspend fun fetchChat(user: User, chatId: String): Result<Chat?>

    suspend fun createChat(user: User, chat: Chat): Result<Void?>

    suspend fun updateChat(user: User, chat: Chat): Result<Void?>

    suspend fun deleteChat(user: User, event: SchoolEvent): Result<Void?>

    suspend fun createMessage(user: User, message: Message): Result<Void?>

}
