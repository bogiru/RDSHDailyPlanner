package com.bogiruapps.rdshapp.notice

import java.util.*

data class Notice (
    var id: String = UUID.randomUUID().toString(),
    var author: String= "",
    var date: Date = Calendar.getInstance().time,
    var text: String = "",
    var title: String = ""
)
