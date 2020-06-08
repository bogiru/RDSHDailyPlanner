package com.bogiruapps.rdshapp.data.user

import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User


interface UserDataSource {

    suspend fun createUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>

    suspend fun fetchUser(userId: String): Result<User?>

    suspend fun fetchUsers(region: Region, city: City, school: School): Result<List<User>>

}