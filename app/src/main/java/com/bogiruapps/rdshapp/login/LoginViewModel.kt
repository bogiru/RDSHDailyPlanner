package com.bogiruapps.rdshapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel : ViewModel() {

    enum class AuthenticationState { AUTHENTICATED, UNAUTHENTICATED }

    private val user = FirebaseUserLiveData()

    val authenticationState = user.map { user ->
        if (user != null) AuthenticationState.AUTHENTICATED
        else AuthenticationState.UNAUTHENTICATED
    }
}

