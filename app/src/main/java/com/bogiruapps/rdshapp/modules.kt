package com.bogiruapps.rdshapp

import com.bogiruapps.rdshapp.MainActivityViewModel
import com.bogiruapps.rdshapp.UserRemoteDataSource
import com.bogiruapps.rdshapp.UserRepository
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.bogiruapps.rdshapp.notice.NoticeViewModel
import com.bogiruapps.rdshapp.notice.notice_detail.NoticeDetailViewModel
import com.bogiruapps.rdshapp.school.SchoolViewModel
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
    viewModel { NoticeDetailViewModel(userRepository = get()) }

}