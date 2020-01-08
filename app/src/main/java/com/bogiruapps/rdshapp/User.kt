package com.bogiruapps.rdshapp


data class User(
    var name: String? = "",
    var email: String? = "",
    var school: String? = ""
) {

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_SCHOOL = "school"
    }

}