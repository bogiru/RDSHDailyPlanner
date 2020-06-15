package com.bogiruapps.rdshapp.schoolEvents.tasksEvent

import com.bogiruapps.rdshapp.user.User
import java.util.*

data class SchoolTaskEvent (
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var completed: Boolean = false,
    var description: String = "",
    var user: User? = User()
)
