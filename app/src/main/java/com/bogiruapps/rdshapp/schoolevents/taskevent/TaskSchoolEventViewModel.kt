package com.bogiruapps.rdshapp.schoolevents.taskevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class TaskSchoolEventViewModel(
    private val userRepository: UserRepository,
    private val schoolEventRepository: SchoolEventRepository)
    : ViewModel() {

    private val _openTaskSchoolEventEdit = MutableLiveData<Event<Unit>>()
    val openTaskSchoolEventEdit: LiveData<Event<Unit>> = _openTaskSchoolEventEdit

    private val _queryTaskSchoolEvent = MutableLiveData<Query>()
    val query: LiveData<Query> = _queryTaskSchoolEvent

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openTaskSchoolEventDeleteFragmentEvent = MutableLiveData<Event<TaskSchoolEvent>>()
    val openTaskEventDeleteFragmentSchoolEvent: LiveData<Event<TaskSchoolEvent>> = _openTaskSchoolEventDeleteFragmentEvent

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    val schoolEvent = schoolEventRepository.currentEvent.value!!
    val user = userRepository.currentUser.value!!

    init {
        fetchFirestoreRecyclerQuery()
    }

    fun checkUserIsAuthorSchoolEvent() =  schoolEvent.author.id == user.id

    fun fetchFirestoreRecyclerQuery() {
        viewModelScope.launch {
            when (val result =
                schoolEventRepository.fetchFirestoreRecyclerQueryTasksSchoolEvent(user)) {
                is Result.Success -> {
                    _queryTaskSchoolEvent.value = result.data
                    _dataLoading.value = false
                }

                is Result.Canceled ->
                    showSnackbar("Ошибка при получении списка задач")

                is Result.Error ->
                    showSnackbar("Ошибка при получении списка задач")
            }
        }
    }

    fun taskCompleted(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            if (taskSchoolEvent.user!!.id == user.id) {
                val task = TaskSchoolEvent(
                    taskSchoolEvent.id,
                    taskSchoolEvent.title,
                    !(taskSchoolEvent.completed),
                    taskSchoolEvent.description,
                    taskSchoolEvent.user
                )

                _dataLoading.value = true

                when (schoolEventRepository.updateTaskSchoolEvent(user, task)) {
                    is Result.Success -> {
                        if (taskSchoolEvent.completed) {
                            schoolEventRepository.currentEvent.value!!.countCompletedTask--
                            user.score--
                        } else {
                            schoolEventRepository.currentEvent.value!!.countCompletedTask++
                            user.score++
                        }

                        when (schoolEventRepository.updateSchoolEvent(user, schoolEvent)) {
                            is Result.Success -> {
                                schoolEventRepository.currentEvent.value = schoolEvent
                                when (userRepository.updateUser(user)) {
                                    is Result.Success ->  {
                                        userRepository.currentUser.value = user
                                        _dataLoading.value = false
                                    }
                                }
                        }
                        }
                    }

                    is Result.Canceled ->
                        showSnackbar("Ошибка при обновлении задачи. Попробуйте снова")

                    is Result.Error ->
                        showSnackbar("Ошибка при обновлении задачи. Попробуйте снова")
                }
            }
        }
    }

    fun deleteTaskSchoolEvent(taskSchoolEvent: TaskSchoolEvent) {
        viewModelScope.launch {
            schoolEventRepository.deleteTaskSchoolEvent(user, taskSchoolEvent)
        }
    }

    fun showTaskSchoolEventEdit() {
        _openTaskSchoolEventEdit.value = Event(Unit)
    }

    fun showSnackbar(message: String) {
        _showSnackbar.value = message
    }

}