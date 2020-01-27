package com.bogiruapps.rdshapp.events

import java.util.*

data class SchoolEvent (
    var id: String,
    var title: String,
    var description: String,
    var deadline: Date,
    var progress: Int
    ) {
    constructor() : this("", "", "", Date(), 0)
}