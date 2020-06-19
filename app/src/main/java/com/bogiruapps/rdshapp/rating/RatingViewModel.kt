package com.bogiruapps.rdshapp.rating

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class RatingViewModel(
    private val application: Application,
    val userRepository: UserRepository
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _queryUsers = MutableLiveData<Query>()
    val queryUsers: LiveData<Query> = _queryUsers

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryUser()) {
                is Result.Success -> {
                    _queryUsers.value = result.data
                    _dataLoading.value = false
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_users_list_from_current_school)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_users_list_from_current_school)
            }
        }
    }


}
