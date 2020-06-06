package com.bogiruapps.rdshapp.di

import com.bogiruapps.rdshapp.MainActivityViewModel
import com.bogiruapps.rdshapp.chats.ChatsViewModel
import com.bogiruapps.rdshapp.data.UserRemoteDataSource
import com.bogiruapps.rdshapp.data.UserRepository
import com.bogiruapps.rdshapp.data.UserRepositoryImpl
import com.bogiruapps.rdshapp.events.EventsViewModel
import com.bogiruapps.rdshapp.chats.chat_room_event.EventChatRoomViewModel
import com.bogiruapps.rdshapp.data.noticeData.NoticeRemoteDataSource
import com.bogiruapps.rdshapp.data.noticeData.NoticeRepository
import com.bogiruapps.rdshapp.data.noticeData.NoticeRepositoryImpl
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
import com.google.firebase.storage.FirebaseStorage

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {

    //Firebase
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    // Data source
    single { UserRemoteDataSource(db = get(), storage = get()) }
    single { NoticeRemoteDataSource(db = get()) }

    // Repository
    single<UserRepository> {
        UserRepositoryImpl(
            dataSource = get()
        )
    }

    single<NoticeRepository> {
        NoticeRepositoryImpl(
            dataSource = get()
        )
    }

    // ViewModels
    viewModel { MainActivityViewModel(application = get(), userRepository = get()) }
    viewModel { NoticeViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { SchoolViewModel(userRepository = get()) }
    viewModel { NoticeEditViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { NoticeDetailViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { EventsViewModel(userRepository = get()) }
    viewModel { EventDetailViewModel(userRepository = get()) }
    viewModel { EventEditViewModel(userRepository = get()) }
    viewModel { TaskEventViewModel(userRepository = get()) }
    viewModel { TaskEventEditViewModel(userRepository = get()) }
    viewModel { RatingViewModel(userRepository = get()) }
    viewModel { UserViewModel(userRepository = get()) }
    viewModel { EventChatRoomViewModel(userRepository = get()) }
    viewModel { ChatsViewModel(userRepository = get()) }

}