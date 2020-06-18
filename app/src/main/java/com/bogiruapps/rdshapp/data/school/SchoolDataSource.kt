package com.bogiruapps.rdshapp.data.school

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result

interface SchoolDataSource {

    suspend fun fetchRegions(): Result<List<Region>>

    suspend fun fetchCities(regionId: String): Result<List<City>>

    suspend fun fetchSchools(regionId: String, cityId: String): Result<List<School>>

}