package com.bogiruapps.rdshapp.notice.notice_detail

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.notice.Notice
import kotlinx.coroutines.launch
import java.util.*

class NoticeDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _openNoticeEditFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeEditFragmentEvent: LiveData<Event<Unit>> = _openNoticeEditFragmentEvent

    private val _openNoticeDeleteFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeDeleteFragmentEvent: LiveData<Event<Unit>> = _openNoticeDeleteFragmentEvent

    private val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>> = _showToast

    val notice = userRepository.currentNotice

    fun deleteNotice() {
        viewModelScope.launch {
            userRepository.deleteNotice()
        }
    }

    fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

    fun showEditNoticeFragment() {
        if (userRepository.currentUser.value!!.name == userRepository.currentNotice.value!!.author) {
            _openNoticeEditFragmentEvent.value = Event(Unit)
        } else {
            _showToast.value = Event("Право редактирование предоставлено только автору объявления")
        }

    }

    fun showDeleteNoticeFragment() {
        if (userRepository.currentUser.value!!.name == userRepository.currentNotice.value!!.author)  _openNoticeDeleteFragmentEvent.value = Event(Unit)
        else _showToast.value = Event("Право удаления предоставлено только автору объявления")

    }

}