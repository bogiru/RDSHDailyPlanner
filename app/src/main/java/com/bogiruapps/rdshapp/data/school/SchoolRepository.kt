package com.bogiruapps.rdshapp.data.school

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result

interface SchoolRepository {

    suspend fun fetchRegions(): Result<List<Region>>

    suspend fun fetchCities(user: User): Result<List<City>>

    suspend fun fetchSchools(user: User): Result<List<School>>

}