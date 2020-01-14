package com.bogiruapps.rdshapp

import com.bogiruapps.rdshapp.school.School


data class User(
    var name: String? = "",
    var email: String? = "",
    var school: School
) {

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_SCHOOL = "school"
    }

}