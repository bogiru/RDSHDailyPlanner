package com.bogiruapps.rdshapp.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import kotlinx.coroutines.launch

class SchoolViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _regions = MutableLiveData<List<Region>>()
    val regions: LiveData<List<Region>> = _regions

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val _schools = MutableLiveData<List<School>>()
    val schools: LiveData<List<School>> = _schools

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showSubmitButton = MutableLiveData<Event<Unit>>()
    val showSubmitButton: LiveData<Event<Unit>> = _showSubmitButton

    private val _resetAddress = MutableLiveData<Event<Unit>>()
    val resetAddress: LiveData<Event<Unit>> = _resetAddress

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    val user = userRepository.currentUser.value!!

    init {
        fetchRegions()
    }

    fun updateUserRegion(region: Region) {
        viewModelScope.launch {
            _dataLoading.value = true
            user.region = region
            when (userRepository.updateUser(user)) {
                is Result.Success -> {
                    userRepository.currentUser.value = user
                    fetchCities()
                }
            }
        }
    }

    fun updateUserCity(city: City) {
        viewModelScope.launch {
            _dataLoading.value = true
            user.city = city
            when (userRepository.updateUser(user)) {
                is Result.Success -> {
                    userRepository.currentUser.value = user
                    fetchSchools()
                }
            }
        }
    }

   fun updateUserSchool(school: School) {
            viewModelScope.launch {
                _dataLoading.value = true
                user.school = school
                when (userRepository.updateUser(user)) {
                    is Result.Success -> {
                        when (userRepository.addUserToSchool()) {
                            is Result.Success -> {
                                _dataLoading.value = false
                                _showSubmitButton.value = Event(Unit)
                            }
                        }
                    }
                }
            }
    }

    fun resetAddress() {
        viewModelScope.launch {
            if (userRepository.currentUser.value!!.school.name != "") {
                when (userRepository.deleteUserFromSchool()) {
                    is Result.Success -> {
                        _resetAddress.value = Event(Unit)
                    }
                }
            } else {
                _resetAddress.value = Event(Unit)
            }
        }
    }

    fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }

    private fun fetchRegions() {
        viewModelScope.launch {
            when (val result = userRepository.fetchRegions()) {
                is Result.Success -> {
                    _dataLoading.value = false
                    _regions.value = result.data
                }
            }
        }
    }

    private fun fetchCities() {
        viewModelScope.launch {
            when (val result = userRepository.fetchCities()) {
                is Result.Success -> {
                    _dataLoading.value = false
                    _cities.value = result.data
                }
            }
        }
    }

    private fun fetchSchools() {
        viewModelScope.launch {
            when (val result = userRepository.fetchSchools()) {
                is Result.Success -> {
                    _schools.value = result.data
                    _dataLoading.value = false
                }
            }
        }
    }

}