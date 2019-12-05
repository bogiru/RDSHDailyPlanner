package com.bogiruapps.rdshapp.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogiruapps.rdshapp.FirestoreRepository

class NoticeViewModelFactory(private val repository: FirestoreRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoticeViewModel::class.java)) {
            return NoticeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}