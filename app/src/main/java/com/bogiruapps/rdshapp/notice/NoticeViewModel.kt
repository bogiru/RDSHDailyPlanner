package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel(private val repository: FirestoreRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance().currentUser

    private var _hasSchool = MutableLiveData<Boolean>(false)
    val hasSchool: LiveData<Boolean>
        get() = _hasSchool

    private var _isCheckSchool = MutableLiveData<Boolean>(false)
    val isCheckSchool: LiveData<Boolean>
        get() = _isCheckSchool

    fun checkHasSchool() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getSchool(auth?.email.toString())
            }
            _hasSchool.value = data != ""
            _isCheckSchool.value = true
        }
    }

}