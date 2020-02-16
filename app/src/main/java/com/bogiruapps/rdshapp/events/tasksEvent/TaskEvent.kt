package com.bogiruapps.rdshapp.events.tasksEvent

import com.bogiruapps.rdshapp.user.User

data class TaskEvent (
    var id: String = "",
    var title: String = "",
    var completed: Boolean = false,
    var description: String = "",
    var user: User? = User()
)
