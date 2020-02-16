package com.bogiruapps.rdshapp.data

import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User


interface UserDataSource {

    suspend fun createUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>


}