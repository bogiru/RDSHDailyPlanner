package com.bogiruapps.rdshapp.user

import com.bogiruapps.rdshapp.school.School
import java.util.*


data class User(
    var name: String? = "",
    var email: String? = "",
    var school: School = School("", ""),
    var score: Int = 0,
    var pictureUrl: String? = "",
    var admin: Boolean? = false,
    var id: String = UUID.randomUUID().toString()
)