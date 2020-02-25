package com.bogiruapps.rdshapp.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class UsersViewModel(val userRepository: UserRepository) : ViewModel() {
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query


    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryUser()) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                }
            }
        }
    }

}
