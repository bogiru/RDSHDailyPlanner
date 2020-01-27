package com.bogiruapps.rdshapp.notice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class NoticeViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user: LiveData<User> = userRepository.currentUser

    private val auth = FirebaseAuth.getInstance().currentUser

    private val _notices = MutableLiveData<List<Notice>>()
    val notices: LiveData<List<Notice>> = _notices

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openChooseSchoolFragmentEvent = MutableLiveData<Event<Unit>>()
    val openChooseSchoolFragmentEvent: LiveData<Event<Unit>> = _openChooseSchoolFragmentEvent

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _openNoticeEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeEditFragmentEvent: LiveData<Event<Unit>> = _openNoticeEditFragmentEvent

    private val _openAlertDialogDeleteEvent = MutableLiveData<Event<Unit>>()
    val openAlertDialogDeleteEvent: LiveData<Event<Unit>> = _openAlertDialogDeleteEvent

    val tempNotice = Notice()

    fun checkUserSchool() {
        val user = userRepository.currentUser.value
        if (user != null) {
            val school = user.school
            if (school.name == "") showSchoolFragment()
            else {
                showNoticeFragment()
            }
        }
    }

    fun checkAuthorNotice(author: String): Boolean {
        return (userRepository.currentUser.value!!.name == author)
    }

    /*fun addNotice(text: String) {
        op
    }*/

   /* fun initNotices() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = userRepository.fetchNotices(userRepository.currentUser.value?.school!!)) {
                is Result.Success -> {
                    _notices.value = result.data

                    _dataLoading.value = false
                }
            }
        }
    }*/

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice> {
        return userRepository.fetchFirestoreRecyclerOptionsNotice()
    }

    fun deleteNotice() {
        viewModelScope.launch {
            userRepository.deleteNotice()
        }
    }

    fun showEditDetailFragment(notice: Notice? = null) {
        userRepository.currentNotice.value = notice
        _openNoticeEditFragmentEvent.value = Event(Unit)
    }

    private fun showSchoolFragment() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

    fun showAlertDialogDelete(notice: Notice?) {
        userRepository.currentNotice.value = notice
        Log.i("Deletee", "showAlert: " + notice!!.id)
        _openAlertDialogDeleteEvent.value = Event(Unit)
    }


}