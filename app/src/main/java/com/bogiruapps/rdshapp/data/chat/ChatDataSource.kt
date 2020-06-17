package com.bogiruapps.rdshapp.data.chat

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result

interface ChatDataSource {

    suspend fun fetchChat(user: User, chatId: String): Result<Chat?>

    suspend fun createChat(user: User, chat: Chat): Result<Void?>

    suspend fun updateChat(user: User, chat: Chat): Result<Void?>

    suspend fun deleteChat(user: User, chat: Chat): Result<Void?>

    suspend fun createMessage(user: User, chatId: String, message: Message): Result<Void?>

}