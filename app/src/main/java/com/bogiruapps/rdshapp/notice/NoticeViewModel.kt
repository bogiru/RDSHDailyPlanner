package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.*
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class NoticeViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user: LiveData<User> = userRepository.currentUser

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
                userRepository.updateNotice(notice)
            }
        }
    }

    private fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result = userRepository.fetchFirestoreRecyclerQueryNotice()) {
                is Result.Success -> {
                    _query.value = result.data
                    _dataLoading.value = false
                    showNoticeFragment()
                }
            }
        }
    }

    fun showDetailNoticeFragment(notice: Notice) {
        userRepository.currentNotice.value = notice
        _openNoticeDetailFragmentEvent.value = Event(Unit)
    }

    fun showEditNoticeFragment() {
        userRepository.stateNotice.value = State.CREATE
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