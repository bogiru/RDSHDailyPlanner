package com.bogiruapps.rdshapp.events

import com.bogiruapps.rdshapp.user.User
import java.util.*

data class SchoolEvent (
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    var deadline: Date = Calendar.getInstance().time,
    var countTask: Int = 0,
    var countCompletedTask: Int = 0,
    var author: User = User(),
    var indexImage: Int = Random().nextInt(25)
)