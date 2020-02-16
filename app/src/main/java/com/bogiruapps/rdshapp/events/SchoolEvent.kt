package com.bogiruapps.rdshapp.events

import java.util.*

data class SchoolEvent (
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var deadline: Date = Date(),
    var countTask: Int = 0,
    var countCompletedTask: Int = 0
    ) {
}