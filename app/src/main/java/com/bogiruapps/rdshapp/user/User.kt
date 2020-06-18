package com.bogiruapps.rdshapp.user

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import java.util.*


data class User(
    var name: String? = "",
    var id: String = UUID.randomUUID().toString(),
    var region: Region = Region("", ""),
    var city: City = City("", ""),
    var school: School = School("", ""),
    var score: Int = 0
)