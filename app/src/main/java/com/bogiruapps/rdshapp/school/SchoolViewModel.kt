package com.bogiruapps.rdshapp.school

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.school.SchoolRepository
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import kotlinx.coroutines.launch

class SchoolViewModel(
    private val userRepository: UserRepository,
    private val schoolRepository: SchoolRepository
): ViewModel() {

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

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

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

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_update_region.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_update_region.toString()
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

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_update_city.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_update_city.toString()
            }
        }
    }

   fun updateUserSchool(school: School) {
            viewModelScope.launch {
                _dataLoading.value = true
                user.school = school
                when (userRepository.updateUser(user)) {
                    is Result.Success -> {
                        when (schoolRepository.addUserToSchool(user)) {
                            is Result.Success -> {
                                _dataLoading.value = false
                                _showSubmitButton.value = Event(Unit)
                            }
                            is Result.Canceled ->
                                _showSnackbar.value = R.string.error_add_user_to_school.toString()

                            is Result.Error ->
                                _showSnackbar.value = R.string.error_add_user_to_school.toString()
                        }
                    }

                    is Result.Canceled ->
                        _showSnackbar.value = R.string.error_update_data_user.toString()

                    is Result.Error ->
                        _showSnackbar.value = R.string.error_update_data_user.toString()
                }
            }
    }

    fun resetAddress() {
        viewModelScope.launch {
            if (userRepository.currentUser.value!!.school.name != "") {
                when (schoolRepository.deleteUserFromSchool(user)) {
                    is Result.Success -> {
                        _resetAddress.value = Event(Unit)
                    }

                    is Result.Canceled ->
                        _showSnackbar.value = R.string.error_update_school.toString()

                    is Result.Error ->
                        _showSnackbar.value = R.string.error_update_school.toString()
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
            when (val result = schoolRepository.fetchRegions()) {
                is Result.Success -> {
                    _dataLoading.value = false
                    _regions.value = result.data
                }

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_regions_list.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_fetch_regions_list.toString()
            }
        }
    }

    private fun fetchCities() {
        viewModelScope.launch {
            when (val result = schoolRepository.fetchCities(user)) {
                is Result.Success -> {
                    _dataLoading.value = false
                    _cities.value = result.data
                }

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_cities_list.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_fetch_cities_list.toString()
            }
        }
    }

    private fun fetchSchools() {
        viewModelScope.launch {
            when (val result = schoolRepository.fetchSchools(user)) {
                is Result.Success -> {
                    _schools.value = result.data
                    _dataLoading.value = false
                }

                is Result.Canceled ->
                    _showSnackbar.value = R.string.error_fetch_schools_list.toString()

                is Result.Error ->
                    _showSnackbar.value = R.string.error_fetch_schools_list.toString()
            }
        }
    }

}