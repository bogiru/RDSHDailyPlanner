package com.bogiruapps.rdshapp.notice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    private val _closeAddNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val closeAddNoticeFragmentEvent: LiveData<Event<Unit>> = _closeAddNoticeFragmentEvent

    fun checkUserSchool(firebaseUser: FirebaseUser?) {
        val user = userRepository.currentUser.value
        if (user != null) {
            val school = user.school
            if (school.name == "") showSchoolFragment()
            else {
                showNoticeFragment()
            }
        }
    }

    fun addNotice(text: String) {
        viewModelScope.launch {
            when (userRepository.createNewNotice(Notice("", text))) {
                is Result.Success -> {
                    hideAddNoticeFragment()
                }
            }
        }
    }

    fun editNotice(notice: Notice) {
        viewModelScope.launch {
            notice.text = "сурок"
            Log.i("Deletee", notice.id)
            when (userRepository.updateNotice(notice)) {
            }
        }
    }

    fun deleteNotice(notice: Notice) {
        viewModelScope.launch {
            Log.i("Deletee", "dfd" + notice.id)
            //userRepository.deleteNotice(notice)
        }
    }

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
        return userRepository.fetchFirestoreRecyclerOptions()
    }

    private fun hideAddNoticeFragment() {
        _closeAddNoticeFragmentEvent.value = Event(Unit)
    }

    private fun showSchoolFragment() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }


}