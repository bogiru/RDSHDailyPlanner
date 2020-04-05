package com.bogiruapps.rdshapp.user

import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.data.UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.Event


class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun fetchCurrentUser() = userRepository.currentUser.value

    private val _showActionPickActivity = MutableLiveData<Event<Unit>>()
    val showActionPickActivity: LiveData<Event<Unit>> = _showActionPickActivity

    fun openActionPickActivity() {
        _showActionPickActivity.value = Event(Unit)
    }
}