package com.bogiruapps.rdshapp.chats

import com.bogiruapps.rdshapp.events.chat_room_event.Message
import java.util.*

data class Chat(
    val id: String = "",
    val title: String = "",
    val lastMessage: Message = Message("Сообщений нет"),
    var indexImage: Int = Random().nextInt(25)
)