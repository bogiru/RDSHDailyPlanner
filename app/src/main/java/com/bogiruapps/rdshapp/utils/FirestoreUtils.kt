package com.bogiruapps.rdshapp.utils
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.school.School
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
                if (isCanceled) cont.cancel() else  cont.resume(
                    Result.Success(
                        result as T
                    )
                )
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

fun QuerySnapshot.toSchoolList(): List<School> = this.map { item ->
    School(item.data["name"].toString(), item.id)
}

fun QuerySnapshot.toUserList(): List<User> = this.map { item -> item.toObject(User::class.java) }
