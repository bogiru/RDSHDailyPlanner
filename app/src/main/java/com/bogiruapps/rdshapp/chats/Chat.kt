package com.bogiruapps.rdshapp.chats

import com.bogiruapps.rdshapp.chats.chatroomevent.Message
import java.util.*

data class Chat(
    val id: String = "",
    var title: String = "",
    var lastMessage: Message = Message("Сообщений нет"),
    var indexImage: Int = Random().nextInt(25)
)