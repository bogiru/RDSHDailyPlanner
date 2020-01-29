package com.bogiruapps.rdshapp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.school.School
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch


class MainActivityViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _openSignInActivityEvent = MutableLiveData<Event<Unit>>()
    val openSignInActivityEvent: LiveData<Event<Unit>> = _openSignInActivityEvent

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    val user: LiveData<User> = userRepository.currentUser

    fun checkUserIsConnected(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            if (user.value == null) {
                fetchCurrentUserInformation(firebaseUser)
            }
        } else {
            showSignInActivity()
        }
    }

    fun handleSignInActivityResult(resultCode: Int, data: Intent?, firebaseUser: FirebaseUser?) {
        if (resultCode == RESULT_OK) {
            checkUserIsConnected(firebaseUser)
        } else {
            val response = IdpResponse.fromResultIntent(data)
            showSignInActivity()
        }
    }

    fun logoutUser(context: Context) {
        AuthUI.getInstance().signOut(context)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userRepository.currentUser.value = null
                    showSignInActivity()
                }
            }
    }

    private fun fetchCurrentUserInformation(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            when(val result = userRepository.fetchUser(firebaseUser.email.toString())) {
                is Result.Success -> {
                    val user = result.data
                    Log.i("QWE", "mainViewModel ${user.toString()}")
                    if (user != null) {
                        setupUserInformation(user)
                    } else {
                        createUserToDb(firebaseUser)
                    }
                }
                is Result.Error ->  Log.i("QWE", "mainViewModel ${result.exception}")
            }
        }
    }

    private fun createUserToDb(firebaseUser: FirebaseUser) {
        val user = User(firebaseUser.displayName, firebaseUser.email, School("", ""))
        viewModelScope.launch {
            when(userRepository.createNewUser(user)) {
                is Result.Success -> fetchCurrentUserInformation(firebaseUser)
                is Result.Error -> Log.d("III", "error")
                is Result.Canceled -> Log.d("III", "canceled")
            }
        }
    }

    private fun setupUserInformation(user: User) {
        userRepository.currentUser.value = user
        //Log.i("QWE", "setupUserInformation: " + )
        showNoticeFragment()
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

    private fun showSignInActivity() {
        _openSignInActivityEvent.value = Event(Unit)
    }
}