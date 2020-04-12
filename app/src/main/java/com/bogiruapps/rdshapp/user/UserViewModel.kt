package com.bogiruapps.rdshapp.user

import android.app.Activity.RESULT_OK
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.data.UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import kotlinx.coroutines.launch
import com.bogiruapps.rdshapp.utils.Result

class UserViewModel(val userRepository: UserRepository) : ViewModel() {

    fun fetchCurrentUser() = userRepository.currentUser.value

    private val _pictureUrlLiveData = MutableLiveData<String>()
    val pictureUrlLiveData: LiveData<String> = _pictureUrlLiveData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showActionPickActivity = MutableLiveData<Event<Unit>>()
    val showActionPickActivity: LiveData<Event<Unit>> = _showActionPickActivity

    val user = userRepository.currentUser.value!!

    init {
        _dataLoading.value = true
        _pictureUrlLiveData.value = user.imageUrl
        _dataLoading.value = false
    }

    fun fetchPictureByUser(resultCode: Int, uri: Uri?) {
        _dataLoading.value = true
        if (resultCode == RESULT_OK) {
            uri?.let {
                downloadPictureToStorage(uri)
            }
        }
    }

    private fun downloadPictureToStorage(uriPicture: Uri) {
        viewModelScope.launch {
            when (val uriStorage = userRepository.updateUserPicture(user, uriPicture)) {
                is Result.Success -> {
                    uriStorage.data?.let { uri ->
                        user.imageUrl = uri.toString()
                        userRepository.currentUser.value = user
                        _pictureUrlLiveData.value = user.imageUrl
                    }
                }
            }
            _dataLoading.value = false
        }
    }


    fun openActionPickActivity() {
        _showActionPickActivity.value = Event(Unit)
    }
}