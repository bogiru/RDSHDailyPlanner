package com.bogiruapps.rdshapp.data.noticeData

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query

interface NoticeRepository {

    val currentNotice: MutableLiveData<Notice>
    val stateNotice: MutableLiveData<State>

    suspend fun createNewNotice(user: User, notice: Notice): Result<Void?>

    suspend fun updateNotice(user: User, notice: Notice): Result<Void?>

    suspend fun deleteNotice(user: User): Result<Void?>

    suspend fun fetchFirestoreRecyclerQueryNotice(user: User): Result<Query>

}