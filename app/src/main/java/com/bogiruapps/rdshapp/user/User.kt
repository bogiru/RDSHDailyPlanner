package com.bogiruapps.rdshapp.user

import com.bogiruapps.rdshapp.school.School
import java.util.*


data class User(
    var name: String? = "",
    var email: String? = "",
    var school: School = School("", "")
)