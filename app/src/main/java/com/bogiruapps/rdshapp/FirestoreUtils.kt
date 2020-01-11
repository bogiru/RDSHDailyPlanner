package com.bogiruapps.rdshapp
import com.bogiruapps.rdshapp.notice.Notice
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Canceled(val exception: Exception) : Result<Nothing>()
}

suspend fun <T> Task<T>.await(): Result<T> {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                Result.Canceled(CancellationException("Task $this was cancelled normally."))
            } else {
                @Suppress("UNCHECKED_CAST")
                (Result.Success(result as T))
            }
        } else {
            Result.Error(e)
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                @Suppress("UNCHECKED_CAST")
                if (isCanceled) cont.cancel() else  cont.resume(Result.Success(result as T))
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}

fun returnSuccessOrError(result: Result<Void?>): Result<Void?> {
    return when (result) {
        is Result.Success -> {
            Result.Success(null)
        }
        is Result.Error, is Result.Canceled -> result
    }
}

fun DocumentSnapshot.toUser(): User? = this.toObject(User::class.java)

fun QuerySnapshot.toSchoolList(): List<String> {
    val items = mutableListOf<String>()
    for (item in this) {
        items.add(item["name"].toString())
    }
    return items
}

fun QuerySnapshot.toNoticeList(): List<Notice> {
    val items = mutableListOf<Notice>()
    for (item in this) {
        items.add(Notice(item["text"].toString()))
    }
    return items
}
