package com.bogiruapps.rdshapp.notice

import com.bogiruapps.rdshapp.user.User
import java.util.*

data class Notice (
    var id: String = UUID.randomUUID().toString(),
    var author: User = User(),
    var date: Date = Calendar.getInstance().time,
    var text: String = "",
    var title: String = "",
    var listOfUsersViewed: MutableList<String> = mutableListOf()
)
