package com.bogiruapps.rdshapp.notice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user: LiveData<User> = userRepository.currentUser

    private val auth = FirebaseAuth.getInstance().currentUser

    private val _notices = MutableLiveData<List<String>>()
    val notices: LiveData<List<String>> = _notices

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        initNotices()
    }

    private fun initNotices() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = userRepository.fetchNotices(userRepository.currentUser.value?.school.toString())) {
                is Result.Success -> {
                    _notices.value = result.data
                    _dataLoading.value = false
                }
            }
        }
    }


}