package com.bogiruapps.rdshapp.notice.notice_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogiruapps.rdshapp.UserRepository

class NoticeEditViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoticeEditViewModel::class.java)) {
                return NoticeEditViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}