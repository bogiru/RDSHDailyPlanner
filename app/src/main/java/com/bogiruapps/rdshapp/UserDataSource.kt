package com.bogiruapps.rdshapp


interface UserDataSource {

    suspend fun createUser(user: User): Result<Void?>

    suspend fun updateUser(user: User): Result<Void?>


}