package com.bogiruapps.rdshapp.user

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.data.user.UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.school.SchoolRepository
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import kotlinx.coroutines.launch
import com.bogiruapps.rdshapp.utils.Result

class UserViewModel(
    val userRepository: UserRepository,
    val schoolRepository: SchoolRepository
): ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openChooseSchoolFragmentEvent = MutableLiveData<Event<Unit>>()
    val openChooseSchoolFragmentEvent: LiveData<Event<Unit>> = _openChooseSchoolFragmentEvent

    private val _showAlertDialogEditSchool = MutableLiveData<Event<Unit>>()
    val showAlertDialogEditSchool: LiveData<Event<Unit>> = _showAlertDialogEditSchool

    private val _showActionPickActivity = MutableLiveData<Event<Unit>>()
    val showActionPickActivity: LiveData<Event<Unit>> = _showActionPickActivity

    private val _imageLoadingToRemoteStorageCompleteEvent = MutableLiveData<Event<Unit>>()
    val imageLoadingToRemoteStorageCompleteEvent: LiveData<Event<Unit>> = _imageLoadingToRemoteStorageCompleteEvent

    val user = userRepository.currentUser.value!!

    fun fetchPictureFromGallery(resultCode: Int, uri: Uri?) {
        _dataLoading.value = true

        if (resultCode == RESULT_OK) {
            uri?.let {
                downloadPictureToRemoteStorage(uri)
            }
        }
    }

    fun deleteUserFromSchool() {
        _dataLoading.value = true
        viewModelScope.launch {
            when(schoolRepository.deleteUserFromSchool(user)) {
                is Result.Success -> {
                    user.apply {
                        region = Region()
                        city = City()
                        school = School()
                    }

                    when (userRepository.updateUser(user)) {
                        is Result.Success -> {
                            userRepository.currentUser.value = user
                            openChooseSchoolFragmentEvent()
                            _dataLoading.value = false
                        }
                    }
                }
            }
        }
    }

    private fun downloadPictureToRemoteStorage(uriPicture: Uri) {
        viewModelScope.launch {
            when (val uriStorage = userRepository
                    .updateUserPicture(userRepository.currentUser.value!!, uriPicture)) {
                is Result.Success -> {
                    uriStorage.data?.let { uri ->
                        user.pictureUrl = uri.toString()
                    }
                    _imageLoadingToRemoteStorageCompleteEvent.value = Event(Unit)
                    _dataLoading.value = false
                }
            }
        }
    }

    fun openActionPickActivity() {
        _showActionPickActivity.value = Event(Unit)
    }

    private fun openChooseSchoolFragmentEvent() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    fun showAlertDialogEditSchool() {
        _showAlertDialogEditSchool.value = Event(Unit)
    }

}