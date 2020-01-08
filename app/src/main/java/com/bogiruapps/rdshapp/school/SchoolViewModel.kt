package com.bogiruapps.rdshapp.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Result
import com.bogiruapps.rdshapp.User
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _schools = MutableLiveData<List<String>>()
    val schools: LiveData<List<String>> = _schools

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        initSchools()
    }

    fun updateSchool(firebaseUser: FirebaseUser, school: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                val user = User(firebaseUser.displayName, firebaseUser.email, school)
                when (userRepository.updateUser(user)) {
                    is Result.Success -> userRepository.currentUser.value = user
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


}