package com.bogiruapps.rdshapp.notice.notice_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch
import java.util.*

class NoticeEditViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openEditNotice = MutableLiveData<Event<Unit>>()
    val openEditNotice: LiveData<Event<Unit>> = _openEditNotice

    private val _closeEditNotice = MutableLiveData<Event<Unit>>()
    val closeEditNotice: LiveData<Event<Unit>> = _closeEditNotice

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    val notice = userRepository.currentNotice


    fun updateNotice(notice: Notice) {
        when (userRepository.stateNotice.value) {
            State.EDIT -> editNotice(notice)
            State.CREATE -> createNotice(notice)
        }
    }

    private fun createNotice(notice: Notice) {
        notice.author = userRepository.currentUser.value!!.name.toString()

        viewModelScope.launch {
            userRepository.createNewNotice(notice)
            openNoticeFragment()
        }
    }

    private fun editNotice(notice: Notice) {
        if (notice.id == "") createNotice(notice)
        else viewModelScope.launch {
            userRepository.updateNotice(notice)
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