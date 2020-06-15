package com.bogiruapps.rdshapp.utils
import com.bogiruapps.rdshapp.chats.Chat
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.school.City
import com.bogiruapps.rdshapp.school.Region
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.school.School
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Canceled(val exception: Exception) : Result<Nothing>()
}

/**
 * Await for completion of an [Task] without blocking a thread
 *
 * Return a [Result] with data or and error
 *
 * This suspending function is cancellable.
 * If the Job of the current coroutine is cancelled or completed while this suspending function is waiting, this function
 * stops waiting for the completion stage and immediately resumes with [CancellationException].
 *
 * Took and modified from "https://github.com/Kotlin/kotlinx.coroutines"
 *
 * @param T Type of [Result] to return
 * @return a [Result] with its data if the task succeed or its error
 *
 * @see Task
 * @see Result
 */
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

    /**
     * Await for completion of an [UploadTask] without blocking a thread
     *
     * Return a [Result] with data or and error
     *
     * This suspending function is cancellable.
     * If the Job of the current coroutine is cancelled or completed while this suspending function is waiting, this function
     * stops waiting for the completion stage and immediately resumes with [CancellationException].
     *
     * Took and modified from "https://github.com/Kotlin/kotlinx.coroutines"
     *
     * @param T Type of [Result] to return
     * @return a [Result] with its data if the task succeed or its error
     *
     * @see UploadTask
     * @see Result
     */
    suspend fun <T> UploadTask.await(): Result<T> {
        // fast path
        if (isComplete) {
            val e = exception
            return if (e == null) {
                if (isCanceled) {
                    Result.Canceled(CancellationException("Task $this was cancelled normally."))
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Result.Success(result as T)
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
                    if (isCanceled) cont.cancel() else cont.resume(Result.Success(result as T))
                } else {
                    cont.resumeWithException(e)
                }
            }
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

fun DocumentSnapshot.toEvent(): SchoolEvent? = this.toObject(SchoolEvent::class.java)

fun DocumentSnapshot.toChat(): Chat? = this.toObject(Chat::class.java)

fun QuerySnapshot.toRegionList(): List<Region> = this.map { item -> item.toObject(Region::class.java) }

fun QuerySnapshot.toCityList(): List<City> = this.map { item -> item.toObject(City::class.java) }

fun QuerySnapshot.toSchoolList(): List<School> = this.map { item -> item.toObject(School::class.java) }

fun QuerySnapshot.toUserList(): List<User> = this.map { item -> item.toObject(User::class.java) }
