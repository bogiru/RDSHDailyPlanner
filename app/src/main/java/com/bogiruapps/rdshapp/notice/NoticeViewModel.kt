package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class NoticeViewModel(
    private val userRepository: UserRepository,
    private val noticeRepository: NoticeRepository) : ViewModel() {

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

    private val _query = MutableLiveData<Query>()
    val query: LiveData<Query> = _query

    fun checkUserSchool() {
        val user = userRepository.currentUser.value
        if (user != null) {
            val school = user.school
            if (school.name == "") {
                showSchoolFragment()
            } else {
                fetchFirestoreRecyclerQuery()
            }
        }
    }

    fun isLookNotice(notice: Notice) {
        viewModelScope.launch {
            if (!notice.views
                    .contains(userRepository.currentUser.value!!.email)
            ) {
                notice.views.add(userRepository.currentUser.value!!.email!!)
                noticeRepository.updateNotice(userRepository.currentUser.value!!, notice)
            }
        }
    }

    private fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = noticeRepository.fetchFirestoreRecyclerQueryNotice(userRepository.currentUser.value!!)) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                    showNoticeFragment()
                }
            }
        }
    }

    fun showDetailNoticeFragment(notice: Notice) {
        noticeRepository.currentNotice.value = notice
        _openNoticeDetailFragmentEvent.value = Event(Unit)
    }

    fun showEditNoticeFragment() {
        noticeRepository.stateNotice.value = State.CREATE
        noticeRepository.currentNotice.value = Notice()
        _openNoticeEditFragmentEvent.value = Event(Unit)
    }

    private fun showSchoolFragment() {
        _openChooseSchoolFragmentEvent.value = Event(Unit)
    }

    private fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }
}