package com.bogiruapps.rdshapp.user

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.data.UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.school.School
import kotlinx.coroutines.launch
import com.bogiruapps.rdshapp.utils.Result

class UserViewModel(val userRepository: UserRepository) : ViewModel() {

    fun fetchCurrentUser() = userRepository.currentUser.value

    private val _dataLoadingImage = MutableLiveData<Boolean>()
    val dataLoadingImage: LiveData<Boolean> = _dataLoadingImage

    private val _openChooseSchoolFragmentEvent = MutableLiveData<Event<Unit>>()
    val openChooseSchoolFragmentEvent: LiveData<Event<Unit>> = _openChooseSchoolFragmentEvent

    private val _showAllertDialogEditSchool = MutableLiveData<Event<Unit>>()
    val showAllertDialogEditSchool: LiveData<Event<Unit>> = _showAllertDialogEditSchool

    private val _showActionPickActivity = MutableLiveData<Event<Unit>>()
    val showActionPickActivity: LiveData<Event<Unit>> = _showActionPickActivity

    val user = userRepository.currentUser.value!!

    fun fetchPictureByUser(resultCode: Int, uri: Uri?) {
        _dataLoadingImage.value = true
        if (resultCode == RESULT_OK) {
            uri?.let {
                downloadPictureToStorage(uri)
            }
        }
    }

    fun deleteUserFromSchool() {
        viewModelScope.launch {
            when(userRepository.deleteUserFromSchool()) {
                is Result.Success -> {
                    val tempUser = User(user.name, user.email, Region("", ""), City("", ""), School("", ""), user.score, user.pictureUrl, user.admin, user.id)

                    when (userRepository.updateUser(tempUser)) {
                        is Result.Success -> {
                            userRepository.currentUser.value = tempUser
                            openChooseSchoolFragmentEvent()
                        }
                    }
                }
            }
        }
    }

    private fun downloadPictureToStorage(uriPicture: Uri) {
        viewModelScope.launch {
            when (val uriStorage = userRepository.updateUserPicture(userRepository.currentUser.value!!, uriPicture)) {
                is Result.Success -> {
                    uriStorage.data?.let { uri ->
                        user.pictureUrl = uri.toString()
                    }
                    _dataLoadingImage.value = false
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

    fun showAllertDialogEditSchool() {
        _showAllertDialogEditSchool.value = Event(Unit)
    }

}