package com.bogiruapps.rdshapp.data.school

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SchoolRepositoryImpl (private val dataSource: SchoolRemoteDataSource): SchoolRepository {

    override suspend fun fetchRegions(): Result<List<Region>> = coroutineScope {
        val task = async { dataSource.fetchRegions() }
        return@coroutineScope task.await()
    }

    override suspend fun fetchCities(user: User): Result<List<City>> = coroutineScope {
        val task = async { dataSource.fetchCities(user.region.id) }
        return@coroutineScope task.await()
    }

    override suspend fun fetchSchools(user: User): Result<List<School>> = coroutineScope {
        val task = async { dataSource.fetchSchools(
            user.region.id,
            user.city.id
        ) }
        return@coroutineScope task.await()
    }
}