package com.bogiruapps.rdshapp.notice.notice_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.notice.Notice
import kotlinx.coroutines.launch

class NoticeDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openEditNotice = MutableLiveData<Event<Unit>>()
    val openEditNotice: LiveData<Event<Unit>> = _openEditNotice

    private val _closeEditNotice = MutableLiveData<Event<Unit>>()
    val closeEditNotice: LiveData<Event<Unit>> = _closeEditNotice

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    val notice = userRepository.currentNotice

    init {
        checkNotice()
    }

    private fun checkNotice() {
        if (userRepository.currentNotice.value!!.id == "") openEditNotice()
    }

    fun createNotice(notice: Notice) {
        Log.i("QWE", notice.text)
        viewModelScope.launch {
            userRepository.createNewNotice(notice)
            openNoticeFragment()
        }
    }

    fun editNotice(notice: Notice) {
        if (notice.id == "") createNotice(notice)
        else viewModelScope.launch {
            userRepository.updateNotice(notice)
            closeEditNotice()
        }
    }

    fun deleteNotice(notice: Notice) {
        viewModelScope.launch {
            userRepository.deleteNotice(notice)
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