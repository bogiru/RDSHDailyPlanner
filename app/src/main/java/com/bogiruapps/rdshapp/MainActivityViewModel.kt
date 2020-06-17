package com.bogiruapps.rdshapp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch


class MainActivityViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _openSignInActivityEvent = MutableLiveData<Event<Unit>>()
    val openSignInActivityEvent: LiveData<Event<Unit>> = _openSignInActivityEvent

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _setVisibilityEmailUnverifiedLayoutEvent = MutableLiveData<Event<Boolean>>()
    val setVisibilityEmailUnverifiedLayoutEvent: LiveData<Event<Boolean>> = _setVisibilityEmailUnverifiedLayoutEvent

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    val user: LiveData<User> = userRepository.currentUser

    fun checkUserIsConnected(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {

            if (firebaseUser.isEmailVerified) {
                _setVisibilityEmailUnverifiedLayoutEvent.value = Event(false)
                if (user.value == null) fetchCurrentUserInformation(firebaseUser)
            } else {
                _setVisibilityEmailUnverifiedLayoutEvent.value = Event(true)
                firebaseUser.sendEmailVerification()
            }
        } else {
            showSignInActivity()
        }
    }

    fun handleSignInActivityResult(
        resultCode: Int,
        firebaseUser1: Intent?,
        firebaseUser: FirebaseUser?
    ) {
        if (resultCode == RESULT_OK) {
            checkUserIsConnected(firebaseUser)
        } else {
            showSignInActivity()
        }
    }

    fun logoutUser(context: Context) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userRepository.currentUser.value = null
                showSignInActivity()
            }
        }
    }

    private fun fetchCurrentUserInformation(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            when (val result = userRepository.fetchUser(firebaseUser.uid)) {
                is Result.Success -> {
                    val user = result.data
                    if (user != null) {
                        setupUserInformation(user)
                    } else {
                        createUserToDb(firebaseUser)
                    }
                }

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_information_about_user.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_fetch_information_about_user.toString()
            }
        }
    }

    private fun createUserToDb(firebaseUser: FirebaseUser) {
        val user = User(firebaseUser.displayName, firebaseUser.uid)
        viewModelScope.launch {
            when (userRepository.createNewUser(user)) {
                is Result.Success -> fetchCurrentUserInformation(firebaseUser)

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_create_user_to_db.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_create_user_to_db.toString()
            }
        }
    }

    private fun setupUserInformation(user: User) {
        userRepository.currentUser.value = user
        showNoticeFragment()
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

    private fun showSignInActivity() {
        _openSignInActivityEvent.value = Event(Unit)
    }
}