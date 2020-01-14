package com.bogiruapps.rdshapp.notice

data class Notice (
    val id: String,
    var text: String
)
{
    constructor() : this("", "")
}