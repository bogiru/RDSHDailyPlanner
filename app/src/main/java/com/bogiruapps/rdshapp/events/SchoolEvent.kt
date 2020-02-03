package com.bogiruapps.rdshapp.events

import java.util.*

data class SchoolEvent (
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var deadline: Date = Date(),
    var amountTask: Int = 0,
    var amountCompletedTask: Int = 0
    ) {
}