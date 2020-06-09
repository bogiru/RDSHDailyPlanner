package com.bogiruapps.rdshapp.notice.noticedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class NoticeDetailViewModel(
    private val userRepository: UserRepository,
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _openNoticeEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeEditFragmentEvent: LiveData<Event<Unit>> = _openNoticeEditFragmentEvent

    private val _openNoticeDeleteFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeDeleteFragmentEvent: LiveData<Event<Unit>> = _openNoticeDeleteFragmentEvent

    private val _showSnackbar = MutableLiveData<Event<String>>()
    val showSnackbar: LiveData<Event<String>> = _showSnackbar

    val notice = noticeRepository.currentNotice.value

    fun deleteNotice() {
        viewModelScope.launch {
            noticeRepository.deleteNotice(userRepository.currentUser.value!!)
        }
    }

    fun showEditNoticeFragment() {
        if (userRepository.currentUser.value!!.email == notice!!.author.email) {
            _openNoticeEditFragmentEvent.value = Event(Unit)
            noticeRepository.stateNotice.value = State.EDIT
        } else {
            _showSnackbar.value = Event("Право редактирование предоставлено только автору объявления")
        }
    }

    fun showDeleteNoticeFragment() {
        if (userRepository.currentUser.value!!.email  == notice!!.author.email) {
            _openNoticeDeleteFragmentEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = Event("Право удаления предоставлено только автору объявления")
        }
    }
}