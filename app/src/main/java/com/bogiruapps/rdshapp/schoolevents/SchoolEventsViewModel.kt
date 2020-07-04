package com.bogiruapps.rdshapp.schoolevents

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class SchoolEventsViewModel(
    private val application: Application,
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _openSchoolEventDetailFragment = MutableLiveData<Event<View>>()
    val openSchoolEventDetailFragment: LiveData<Event<View>> = _openSchoolEventDetailFragment

    private val _openSchoolEventEditFragment = MutableLiveData<Event<Unit>>()
    val openSchoolEventEditFragment: LiveData<Event<Unit>> = _openSchoolEventEditFragment

    private val _showSchoolEventContent = MutableLiveData<Event<Unit>>()
    val showSchoolEventContent: LiveData<Event<Unit>> = _showSchoolEventContent

    private val _querySchoolEvents = MutableLiveData<Query>()
    val querySchoolEvents: LiveData<Query> = _querySchoolEvents

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: MutableLiveData<String> = _showSnackbar

    private var user = userRepository.currentUser.value

    fun fetchFirestoreRecyclerQuerySchoolEvents() {
            _dataLoading.value = true
        user?.let {
            viewModelScope.launch {
                when (val result = schoolEventRepository
                    .fetchFirestoreRecyclerQuerySchoolEvents(user!!)) {
                    is Result.Success -> {
                        _querySchoolEvents.value = result.data
                        _dataLoading.value = false
                        showSchoolEventContent()
                    }

                    is Result.Canceled ->
                        _showSnackbar.value = application.resources
                            .getString(R.string.error_fetch_school_events_list)

                    is Result.Error ->
                        _showSnackbar.value = application.resources
                            .getString(R.string.error_fetch_school_events_list)
                }
            }
        }
    }

    fun showDetailSchoolEventFragment(schoolEvent: SchoolEvent, view: View) {
        schoolEventRepository.currentEvent.value = schoolEvent

        viewModelScope.launch {
            when (val result = chatRepository
                .fetchChat(user!!, schoolEvent.id)) {
                is Result.Success -> {
                    chatRepository.currentChat.value = result.data
                    _openSchoolEventDetailFragment.value = Event(view)
                }

                is Result.Canceled ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_information_about_school_event)

                is Result.Error ->
                    _showSnackbar.value = application.resources
                        .getString(R.string.error_fetch_information_about_school_event)
            }
        }
    }

    fun showCreateSchoolEventFragment() {
        schoolEventRepository.stateEvent.value = State.CREATE
        schoolEventRepository.currentEvent.value = SchoolEvent()
        _openSchoolEventEditFragment.value = Event(Unit)
    }

    private fun showSchoolEventContent() {
        _showSchoolEventContent.value = Event(Unit)
    }


}