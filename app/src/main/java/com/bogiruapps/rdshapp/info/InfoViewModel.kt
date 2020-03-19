package com.bogiruapps.rdshapp.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogiruapps.rdshapp.data.UserRepository

class InfoViewModel() : ViewModel() {
    private val _setupPageInfo = MutableLiveData<Int>()
    val setupPageInfo: LiveData<Int> = _setupPageInfo

    fun setupPage(index: Int) {
        //_setupPageInfo.value = index
    }
}