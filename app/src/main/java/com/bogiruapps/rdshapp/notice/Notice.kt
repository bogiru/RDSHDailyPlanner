package com.bogiruapps.rdshapp.notice

import java.util.*

data class Notice (
    var id: String,
    var author: String,
    var date: Date,
    var importance: Int,
    var text: String,
    var title: String
    )
{
    constructor() : this("", "", Date(), 0, "", "")
}