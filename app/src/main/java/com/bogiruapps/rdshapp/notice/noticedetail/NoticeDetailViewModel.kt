package com.bogiruapps.rdshapp.notice.noticedetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class NoticeDetailViewModel(
    private val application: Application,
    userRepository: UserRepository,
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _openNoticeEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeEditFragmentEvent: LiveData<Event<Unit>> = _openNoticeEditFragmentEvent

    private val _openNoticeDeleteFragmentEvent = MutableLiveData<Event<Unit>>()
    val openDialogDeleteNoticeEvent: LiveData<Event<Unit>> = _openNoticeDeleteFragmentEvent

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val notice = noticeRepository.currentNotice.value
    private val user = userRepository.currentUser.value!!

    fun deleteNotice() {
        _dataLoading.value = true
        viewModelScope.launch {
            when (noticeRepository.deleteNotice(user)) {
                is Result.Success -> _dataLoading.value = false

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_delete_notice_to_db)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_delete_notice_to_db)
            }
        }
    }

    fun showEditNoticeFragment() {
        if (user.id == notice!!.author.id) {
            _openNoticeEditFragmentEvent.value = Event(Unit)
            noticeRepository.stateNotice.value = State.EDIT
        } else {
            _showSnackbar.value = application.resources
                .getString(R.string.error_not_enough_rights_to_edit)
        }
    }

    fun showDeleteNoticeFragment() {
        if (user.id  == notice!!.author.id) {
            _openNoticeDeleteFragmentEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = application.resources
                .getString(R.string.error_not_enough_rights_to_edit)
        }
    }
}