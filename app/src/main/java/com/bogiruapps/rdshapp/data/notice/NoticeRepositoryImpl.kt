package com.bogiruapps.rdshapp.data.notice

import androidx.lifecycle.MutableLiveData
import com.bogiruapps.rdshapp.notice.Notice
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.Result
import com.bogiruapps.rdshapp.utils.State
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class NoticeRepositoryImpl (private val dataSource: NoticeRemoteDataSource) : NoticeRepository {

    override val currentNotice = MutableLiveData<Notice>()
    override val stateNotice = MutableLiveData<State>()

    override suspend fun createNewNotice(user: com.bogiruapps.rdshapp.user.User, notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.createNotice(
            user.region,
            user.city,
            user.school,
            notice
        )}
        return@coroutineScope task.await()
    }

    override suspend fun updateNotice(user: User, notice: Notice): Result<Void?> = coroutineScope {
        val task = async { dataSource.updateNotice(
            user.region,
            user.city,
            user.school,
            notice
        )}
        return@coroutineScope task.await()
    }

    override suspend fun deleteNotice(user: User): Result<Void?> = coroutineScope {
        val task = async { dataSource.deleteNotice(
            user.region,
            user.city,
            user.school,
            currentNotice.value!!)}
        return@coroutineScope task.await()
    }

    override suspend fun fetchFirestoreRecyclerQueryNotice(user: User): Result<Query> = coroutineScope {
        val task = async { dataSource.fetchFirestoreRecyclerQueryNotice(
            user.region,
            user.city,
            user.school
        )}
        return@coroutineScope (task.await())
    }

}