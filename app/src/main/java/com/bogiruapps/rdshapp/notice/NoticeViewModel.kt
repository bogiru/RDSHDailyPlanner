package com.bogiruapps.rdshapp.notice

import android.util.Log
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

    private var _isLoadSchools = MutableLiveData<Boolean>(false)
    val isLoadSchools: LiveData<Boolean>
        get() = _isLoadSchools

    var notices = listOf<Notice>()

    fun checkHasSchool() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                Log.i("QWE", "noticeViewModel")
                repository.getSchool(auth?.email.toString())
            }
            _hasSchool.value = data != ""
            _isCheckSchool.value = true
        }
    }

    fun getNotices() {
        viewModelScope.launch {
            notices = withContext(Dispatchers.IO) {
                repository.getNotices()
            }
            _isLoadSchools.value = true
        }
    }

}