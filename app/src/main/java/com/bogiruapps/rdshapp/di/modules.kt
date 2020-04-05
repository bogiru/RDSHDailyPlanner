package com.bogiruapps.rdshapp.di

import com.bogiruapps.rdshapp.MainActivityViewModel
import com.bogiruapps.rdshapp.data.UserRemoteDataSource
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.data.UserRepositoryImpl
import com.bogiruapps.rdshapp.events.EventsViewModel
import com.bogiruapps.rdshapp.events.detail_event.EventDetailViewModel
import com.bogiruapps.rdshapp.events.edit_event.EventEditViewModel
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEventViewModel
import com.bogiruapps.rdshapp.events.tasksEvent.taskEventEdit.TaskEventEditViewModel
import com.bogiruapps.rdshapp.notice.NoticeViewModel
import com.bogiruapps.rdshapp.notice.notice_detail.NoticeDetailViewModel
import com.bogiruapps.rdshapp.notice.notice_edit.NoticeEditViewModel
import com.bogiruapps.rdshapp.school.SchoolViewModel
import com.bogiruapps.rdshapp.rating.RatingViewModel
import com.bogiruapps.rdshapp.user.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {

    //Firebase
    single { FirebaseFirestore.getInstance() }

    // Data source
    single { UserRemoteDataSource(db = get()) }

    // Repository
    single<UserRepository> {
        UserRepositoryImpl(
            dataSource = get()
        )
    }

    // ViewModels
    viewModel { MainActivityViewModel(userRepository = get()) }
    viewModel { NoticeViewModel(userRepository = get()) }
    viewModel { SchoolViewModel(userRepository = get()) }
    viewModel { NoticeEditViewModel(userRepository = get()) }
    viewModel { NoticeDetailViewModel(userRepository = get()) }
    viewModel { EventsViewModel(userRepository = get()) }
    viewModel { EventDetailViewModel(userRepository = get()) }
    viewModel { EventEditViewModel(userRepository = get()) }
    viewModel { TaskEventViewModel(userRepository = get()) }
    viewModel { TaskEventEditViewModel(userRepository = get()) }
    viewModel { RatingViewModel(userRepository = get()) }
    viewModel { UserViewModel(userRepository = get()) }

}