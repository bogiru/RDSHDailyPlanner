package com.bogiruapps.rdshapp.notice.noticeedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import kotlinx.coroutines.launch

class NoticeEditViewModel(
    private val userRepository: UserRepository,
    private val noticeRepository: NoticeRepository) : ViewModel() {

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    val notice = noticeRepository.currentNotice
    private val user = userRepository.currentUser.value!!

    fun checkCreateNoticeStatus(): Boolean = noticeRepository.stateNotice.value == State.CREATE

    fun updateNotice(notice: Notice) {
        if (notice.title == "" || notice.text == "") {
            _showSnackbar.value = "Не все поля заполнены"
        } else {
            _dataLoading.value = true
            when (noticeRepository.stateNotice.value) {
                State.EDIT -> editNotice(notice)
                State.CREATE -> createNotice(notice)
            }
        }
    }

    private fun createNotice(notice: Notice) {
        notice.author = user

        viewModelScope.launch {
            when (noticeRepository.createNewNotice(user, notice)) {
                is Result.Success -> {
                    _dataLoading.value = false
                    openNoticeFragment()
                }

                is Result.Canceled ->
                    _showSnackbar.value = "Ошибка при создании объявления. Попробуйте снова"

                is Result.Error ->
                    _showSnackbar.value = "Ошибка при создании объявления. Попробуйте снова"
            }
        }
    }

    private fun editNotice(notice: Notice) {
        if (notice.id == "") {
            createNotice(notice)
        } else {
            viewModelScope.launch {
                when (noticeRepository.updateNotice(user, notice)) {
                    is Result.Success -> {
                        _dataLoading.value = false
                        openNoticeFragment()
                    }

                    is Result.Canceled ->
                        _showSnackbar.value = "Ошибка при обновлении объявления. Попробуйте снова"

                    is Result.Error ->
                        _showSnackbar.value = "Ошибка при обновлении объявления. Попробуйте снова"
                }
            }
        }
    }

    private fun openNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

}