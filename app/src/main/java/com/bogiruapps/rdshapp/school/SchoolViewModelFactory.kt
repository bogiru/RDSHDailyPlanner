package com.bogiruapps.rdshapp.school

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogiruapps.rdshapp.FirestoreRepository

class SchoolViewModelFactory(private val repository: FirestoreRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolViewModel::class.java)) {
            return SchoolViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}