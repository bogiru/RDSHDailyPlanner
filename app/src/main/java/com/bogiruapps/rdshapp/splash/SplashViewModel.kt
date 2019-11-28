package com.bogiruapps.rdshapp.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SplashViewModel : ViewModel() {
    val isChangeSchool = MutableLiveData<Boolean>(false)

    //create boolean checkHasSchool
    private val _hasSchool = MutableLiveData<Boolean>(false)
    val hasSchool: LiveData<Boolean>
        get() = _hasSchool

    //coroutine
    private var job = Job()
    private  val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    fun checkHasSchool() {
        coroutineScope.launch {
            initHasSchool()
        }

    }

    private suspend fun initHasSchool() {
        val db = FirebaseFirestore.getInstance()
        val authUser = FirebaseAuth.getInstance().currentUser
        val doc = db.collection("users").document(authUser?.email.toString())

        try {
            val result = doc.get().await()
            _hasSchool.value = result["school"] != ""
            Log.i("QWE", result["school"].toString())
            isChangeSchool.value = true
        }catch (e: FirebaseFirestoreException) {}

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}