package com.bogiruapps.rdshapp.user

import com.bogiruapps.rdshapp.school.School
import java.util.*


data class User(
    var name: String? = "",
    var email: String? = "",
    var avatar: String? = "",
    var school: School = School("", ""),
    var score: Int = 0
)