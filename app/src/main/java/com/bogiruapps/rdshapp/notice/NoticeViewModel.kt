package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
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
        viewModelScope.launch {
            when(val result = userRepository.fetchUser(firebaseUser?.email.toString())) {
                is Result.Success -> {
                    val user = result.data
                    if (user != null) {
                        val school = user.school

                        if (school == "") showSchoolFragment()
                        else showNoticeFragment()
                    }
                }
            }
        }
    }

    fun addNotice(text: String) {
        viewModelScope.launch {
            when (val result = userRepository.currentUser.value?.school?.let {
                userRepository.createNewNotice(
                    it, Notice(text))
            }) {
                is Result.Success -> hideAddNoticeFragment()
            }
        }
    }

    fun initNotices() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (val result = userRepository.fetchNotices(userRepository.currentUser.value?.school.toString())) {
                is Result.Success -> {
                    _notices.value = result.data
                    _dataLoading.value = false
                }
            }
        }
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