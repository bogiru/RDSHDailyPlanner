package com.bogiruapps.rdshapp.school

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.Result
import com.bogiruapps.rdshapp.User
import com.bogiruapps.rdshapp.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SchoolViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _schools = MutableLiveData<List<School>>()
    val schools: LiveData<List<School>> = _schools

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    init {
        initSchools()
    }

    fun updateSchool(firebaseUser: FirebaseUser, school: School) {
            viewModelScope.launch {
                val user = User(firebaseUser.displayName, firebaseUser.email, school)
                when (userRepository.updateUser(user)) {
                    is Result.Success -> {
                        userRepository.currentUser.value = user
                        userRepository.addUserToSchool()
                        showNoticeFragment()
                    }
                }
            }
    }

    private fun initSchools() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = userRepository.fetchSchools()) {
                is Result.Success -> {
                    _schools.value = result.data
                    _dataLoading.value = false
                }
            }
        }
        }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }


}