package com.bogiruapps.rdshapp.data.chatData

import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.chats.chat_room_event.Message
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result

interface ChatDataSource {

    suspend fun fetchChat(region: Region, city: City, school: School, chatId: String): Result<Chat?>

    suspend fun createChat(region: Region, city: City, school: School, chat: Chat): Result<Void?>

    suspend fun updateChat(region: Region, city: City, school: School, chat: Chat): Result<Void?>

    suspend fun deleteChat(region: Region, city: City, school: School, event: SchoolEvent, chat: Chat): Result<Void?>

    suspend fun createMessage(region: Region, city: City, school: School, chatId: String, message: Message): Result<Void?>

}