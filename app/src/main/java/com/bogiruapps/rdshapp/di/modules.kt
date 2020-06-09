package com.bogiruapps.rdshapp.di

import com.bogiruapps.rdshapp.MainActivityViewModel
import com.bogiruapps.rdshapp.chats.ChatsViewModel
import com.bogiruapps.rdshapp.data.user.UserRemoteDataSource
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.user.UserRepositoryImpl
import com.bogiruapps.rdshapp.events.EventsViewModel
import com.bogiruapps.rdshapp.chats.chat_room_event.ChatRoomViewModel
import com.bogiruapps.rdshapp.data.chat.ChatRemoteDataSource
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepositoryImpl
import com.bogiruapps.rdshapp.data.event.EventRemoteDataSource
import com.bogiruapps.rdshapp.data.event.EventRepository
import com.bogiruapps.rdshapp.data.event.EventRepositoryImpl
import com.bogiruapps.rdshapp.data.notice.NoticeRemoteDataSource
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepositoryImpl
import com.bogiruapps.rdshapp.data.school.SchoolRemoteDataSource
import com.bogiruapps.rdshapp.data.school.SchoolRepository
import com.bogiruapps.rdshapp.data.school.SchoolRepositoryImpl
import com.bogiruapps.rdshapp.events.detail_event.EventDetailViewModel
import com.bogiruapps.rdshapp.events.edit_event.EventEditViewModel
import com.bogiruapps.rdshapp.events.tasksEvent.TaskEventViewModel
import com.bogiruapps.rdshapp.events.tasksEvent.taskEventEdit.TaskEventEditViewModel
import com.bogiruapps.rdshapp.notice.NoticeViewModel
import com.bogiruapps.rdshapp.notice.noticedetail.NoticeDetailViewModel
import com.bogiruapps.rdshapp.notice.noticeedit.NoticeEditViewModel
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
    single { EventRemoteDataSource(db = get(), storage = get()) }
    single { ChatRemoteDataSource(db = get()) }
    single { SchoolRemoteDataSource(db = get()) }

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

    single<EventRepository> {
        EventRepositoryImpl(
            dataSource = get()
        )
    }

    single<ChatRepository> {
        ChatRepositoryImpl(
            dataSource = get()
        )
    }

    single<SchoolRepository> {
        SchoolRepositoryImpl(
            dataSource = get()
        )
    }

    // ViewModels
    viewModel { MainActivityViewModel(userRepository = get()) }
    viewModel { NoticeViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { SchoolViewModel(userRepository = get(), schoolRepository = get()) }
    viewModel { NoticeEditViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { NoticeDetailViewModel(userRepository = get(), noticeRepository = get()) }
    viewModel { EventsViewModel(userRepository = get(), eventRepository = get(), chatRepository = get()) }
    viewModel { EventDetailViewModel(userRepository = get(), eventRepository = get(), chatRepository = get()) }
    viewModel { EventEditViewModel(userRepository = get(), eventRepository = get(), chatRepository = get()) }
    viewModel { TaskEventViewModel(userRepository = get(), eventRepository = get()) }
    viewModel { TaskEventEditViewModel(userRepository = get(), eventRepository = get()) }
    viewModel { RatingViewModel(userRepository = get()) }
    viewModel { UserViewModel(userRepository = get(), schoolRepository = get()) }
    viewModel { ChatRoomViewModel(userRepository = get(), chatRepository = get()) }
    viewModel { ChatsViewModel(userRepository = get(), chatRepository = get()) }

}