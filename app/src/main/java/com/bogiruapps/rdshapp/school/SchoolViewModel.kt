package com.bogiruapps.rdshapp.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.data.UserRepository
import com.google.firebase.auth.FirebaseUser
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

    private val _showNextButton = MutableLiveData<Event<Unit>>()
    val showNextButton: LiveData<Event<Unit>> = _showNextButton

    private val _openNoticeFragmentEvent = MutableLiveData<Event<Unit>>()
    val openNoticeFragmentEvent: LiveData<Event<Unit>> = _openNoticeFragmentEvent

    val user = userRepository.currentUser.value!!

    init {
        fetchRegions()
    }

    fun fetchRegions() {
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

    fun fetchSchools() {
        viewModelScope.launch {
            when (val result = userRepository.fetchSchools()) {
                is Result.Success -> {
                    _schools.value = result.data
                    _dataLoading.value = false
                }
            }
        }
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
                                _showNextButton.value = Event(Unit)
                            }
                        }
                    }
                }
            }
    }

    fun showNoticeFragment() {
        _openNoticeFragmentEvent.value = Event(Unit)
    }


}