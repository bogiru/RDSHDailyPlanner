package com.bogiruapps.rdshapp.notice.noticedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.utils.Result
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
                    _showSnackbar.value = "Ошибка при удалении объявления. Попробуйте снова"

                is Result.Error ->
                    _showSnackbar.value = "Ошибка при удалении объявления. Попробуйте снова"
            }
        }
    }

    fun showEditNoticeFragment() {
        if (user.id == notice!!.author.id) {
            _openNoticeEditFragmentEvent.value = Event(Unit)
            noticeRepository.stateNotice.value = State.EDIT
        } else {
            _showSnackbar.value = "Право редактирование предоставлено только автору объявления"
        }
    }

    fun showDeleteNoticeFragment() {
        if (user.id  == notice!!.author.id) {
            _openNoticeDeleteFragmentEvent.value = Event(Unit)
        } else {
            _showSnackbar.value = "Право удаления предоставлено только автору объявления"
        }
    }
}