package com.bogiruapps.rdshapp.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolViewModel(val repository: FirestoreRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isFinishLoad = MutableLiveData<Boolean>(false)
    val isFinishLoad: LiveData<Boolean>
        get() = _isFinishLoad

    var schools = listOf("")

    fun loadSchools() {
        viewModelScope.launch {
            schools = withContext(Dispatchers.IO) {
                 repository.getSchools()
            }
            _isFinishLoad.value = true
        }
    }

    fun setSchool(school: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.setSchool(auth.currentUser?.email.toString(), school)
            }
        }
    }


}