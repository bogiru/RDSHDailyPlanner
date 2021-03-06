package com.bogiruapps.rdshapp.di

import com.bogiruapps.rdshapp.MainActivityViewModel
import com.bogiruapps.rdshapp.chats.ChatsViewModel
import com.bogiruapps.rdshapp.data.user.UserRemoteDataSource
import com.bogiruapps.rdshapp.data.user.UserRepository
import com.bogiruapps.rdshapp.data.user.UserRepositoryImpl
import com.bogiruapps.rdshapp.schoolevents.SchoolEventsViewModel
import com.bogiruapps.rdshapp.chats.chatroomevent.ChatRoomViewModel
import com.bogiruapps.rdshapp.data.chat.ChatRemoteDataSource
import com.bogiruapps.rdshapp.data.chat.ChatRepository
import com.bogiruapps.rdshapp.data.chat.ChatRepositoryImpl
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRemoteDataSource
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepository
import com.bogiruapps.rdshapp.data.schoolEvent.SchoolEventRepositoryImpl
import com.bogiruapps.rdshapp.data.notice.NoticeRemoteDataSource
import com.bogiruapps.rdshapp.data.notice.NoticeRepository
import com.bogiruapps.rdshapp.data.notice.NoticeRepositoryImpl
import com.bogiruapps.rdshapp.data.school.SchoolRemoteDataSource
import com.bogiruapps.rdshapp.data.school.SchoolRepository
import com.bogiruapps.rdshapp.data.school.SchoolRepositoryImpl
import com.bogiruapps.rdshapp.schoolevents.detailevent.SchoolEventDetailViewModel
import com.bogiruapps.rdshapp.schoolevents.editevent.SchoolEventEditViewModel
import com.bogiruapps.rdshapp.schoolevents.taskevent.TaskSchoolEventViewModel
import com.bogiruapps.rdshapp.schoolevents.taskevent.taskEventEdit.TaskSchoolEventEditViewModel
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
    single { SchoolEventRemoteDataSource(db = get(), storage = get()) }
    single { NoticeRemoteDataSource(db = get()) }
    single { ChatRemoteDataSource(db = get()) }
    single { SchoolRemoteDataSource(db = get()) }

    // Repository
    single<UserRepository> {
        UserRepositoryImpl(
            dataSource = get()
        )
    }
    single<SchoolEventRepository> {
        SchoolEventRepositoryImpl(
            dataSource = get()
        )
    }

    single<NoticeRepository> {
        NoticeRepositoryImpl(
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
    viewModel { SchoolEventsViewModel(application = get(), userRepository = get(), schoolEventRepository = get(), chatRepository = get()) }
    viewModel { SchoolEventDetailViewModel(application = get(), userRepository = get(), schoolEventRepository = get(), chatRepository = get()) }
    viewModel { SchoolEventEditViewModel(application = get(), userRepository = get(), schoolEventRepository = get(), chatRepository = get()) }
    viewModel { TaskSchoolEventViewModel(application = get(), userRepository = get(), schoolEventRepository = get()) }
    viewModel { TaskSchoolEventEditViewModel(application = get(), userRepository = get(), schoolEventRepository = get()) }
    viewModel { RatingViewModel(application = get(), userRepository = get()) }
    viewModel { UserViewModel(application = get(), userRepository = get()) }
    viewModel { ChatRoomViewModel(application = get(), userRepository = get(), chatRepository = get()) }
    viewModel { ChatsViewModel(application = get(), userRepository = get(), chatRepository = get()) }
    viewModel { NoticeViewModel(application = get(), userRepository = get(), noticeRepository = get()) }
    viewModel { SchoolViewModel(application = get(), userRepository = get(), schoolRepository = get()) }
    viewModel { NoticeEditViewModel(application = get(), userRepository = get(), noticeRepository = get()) }
    viewModel { NoticeDetailViewModel(application = get(), userRepository = get(), noticeRepository = get()) }

}