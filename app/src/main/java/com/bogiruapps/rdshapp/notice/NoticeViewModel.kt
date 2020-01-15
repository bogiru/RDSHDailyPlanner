package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

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

    private val _openNoticeDetailFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeDetailFragmentEvent: LiveData<Event<Unit>> = _openNoticeDetailFragmentEvent

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
        return userRepository.fetchFirestoreRecyclerOptions()
    }

    fun openNoticeDetailFragment(notice: Notice? = null) {
        userRepository.currentNotice.value = notice
        _openNoticeDetailFragmentEvent.value = Event(Unit)
    }

    private fun showSchoolFragment() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }


}