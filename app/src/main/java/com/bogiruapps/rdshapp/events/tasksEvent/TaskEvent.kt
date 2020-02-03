package com.bogiruapps.rdshapp.events.tasksEvent

import com.bogiruapps.rdshapp.User
import java.util.*

data class TaskEvent (
    var id: String = "",
    var title: String = "",
    var compl: Boolean = true,
    var description: String = "",
    var user: User = User())
