package com.bogiruapps.rdshapp.events

import java.util.*

data class SchoolEvent (
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    var deadline: Date = Calendar.getInstance().time,
    var countTask: Int = 0,
    var countCompletedTask: Int = 0
)