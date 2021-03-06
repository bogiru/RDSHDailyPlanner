package com.bogiruapps.rdshapp.notice

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class NoticeViewModel(
    private val application: Application,
    private val userRepository: UserRepository,
    private val noticeRepository: NoticeRepository
) : ViewModel() {

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

    private val _queryNotices = MutableLiveData<Query>()
    val queryNotices: LiveData<Query> = _queryNotices

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private val  user = userRepository.currentUser.value

    fun checkUserSchool() {
        user?.let {
            val school = user.school
            if (school.name == "") {
                showSchoolFragment()
            } else {
                fetchFirestoreRecyclerQueryNotices()
            }
        }
    }

    private fun fetchFirestoreRecyclerQueryNotices() {
        viewModelScope.launch {
            when (val result = noticeRepository
                .fetchFirestoreRecyclerQueryNotice(user!!)) {
                is Result.Success -> {
                    _queryNotices.value = result.data
                    _dataLoading.value = false
                    showNoticeFragment()
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString( R.string.error_fetch_notices_list)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString( R.string.error_fetch_notices_list)
            }
        }
    }

    fun showDetailNoticeFragment(notice: Notice) {
        noticeRepository.currentNotice.value = notice
        _openNoticeDetailFragmentEvent.value = Event(Unit)
    }

    fun showCreateNoticeFragment() {
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