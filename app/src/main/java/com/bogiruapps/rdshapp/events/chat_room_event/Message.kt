package com.bogiruapps.rdshapp.events.chat_room_event

import com.bogiruapps.rdshapp.user.User
import java.util.*

data class Message (
    val text: String? = "",
    val author: User? = User(),
    val date: Date = Calendar.getInstance().time
)