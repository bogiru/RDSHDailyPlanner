package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.*
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.user.User
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

    private val _openNoticeEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeEditFragmentEvent: LiveData<Event<Unit>> = _openNoticeEditFragmentEvent

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

    fun checkAuthorNotice(author: String): Boolean {
        return (userRepository.currentUser.value!!.name == author)
    }

    fun fetchFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice> {
        return userRepository.fetchFirestoreRecyclerOptionsNotice()
    }

    fun showDetailNoticeFragment(notice: Notice) {
        userRepository.currentNotice.value = notice
        _openNoticeDetailFragmentEvent.value = Event(Unit)
    }

    fun showEditNoticeFragment() {
        userRepository.currentNotice.value = Notice()
        _openNoticeEditFragmentEvent.value = Event(Unit)
    }

    private fun showSchoolFragment() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }




}