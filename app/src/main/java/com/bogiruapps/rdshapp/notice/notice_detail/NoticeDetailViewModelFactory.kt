package com.bogiruapps.rdshapp.notice.notice_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogiruapps.rdshapp.UserRepository

class NoticeDetailViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoticeDetailViewModel::class.java)) {
                return NoticeDetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}