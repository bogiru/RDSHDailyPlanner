package com.bogiruapps.rdshapp.notice

import java.util.*

data class Notice (
    var id: String,
    var author: String,
    var date: Date,
    var text: String,
    var title: String
    )
{
    constructor() : this("", "", Date(), "", "")
}