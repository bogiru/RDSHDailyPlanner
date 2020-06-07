package com.bogiruapps.rdshapp.notice.notice_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class NoticeEditViewModel(
    private val userRepository: UserRepository,
    private val noticeRepository: NoticeRepository) : ViewModel() {

    private val _openEditNotice = MutableLiveData<Event<Unit>>()
    val openEditNotice: LiveData<Event<Unit>> = _openEditNotice

    private val _closeEditNotice = MutableLiveData<Event<Unit>>()
    val closeEditNotice: LiveData<Event<Unit>> = _closeEditNotice

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    val notice = noticeRepository.currentNotice

    fun checkCreateNoticeStatus(): Boolean = noticeRepository.stateNotice.value == State.CREATE

    fun updateNotice(notice: Notice) {
        if (notice.title == "" || notice.text == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            when (noticeRepository.stateNotice.value) {
                State.EDIT -> editNotice(notice)
                State.CREATE -> createNotice(notice)
            }
        }
    }

    private fun createNotice(notice: Notice) {
        notice.author = userRepository.currentUser.value!!

        viewModelScope.launch {
            noticeRepository.createNewNotice(userRepository.currentUser.value!!, notice)
            openNoticeFragment()
        }
    }

    private fun editNotice(notice: Notice) {
        if (notice.id == "") createNotice(notice)
        else viewModelScope.launch {
            noticeRepository.updateNotice(userRepository.currentUser.value!!, notice)
            //closeEditNotice()
            openNoticeFragment()
        }
    }

    fun openEditNotice() {
        _openEditNotice.value = Event(Unit)
    }

    private fun closeEditNotice() {
        _closeEditNotice.value = Event(Unit)
    }

    private fun openNoticeFragment() {

        _openNoticeFragmentEvent.value = Event(Unit)
    }

}