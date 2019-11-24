package com.bogiruapps.rdshapp.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*

class SplashViewModel : ViewModel() {

    //create boolean flag
    val isChangeSchool = MutableLiveData<Boolean>()

    //create boolean hasSchool
    private val _hasSchool = MutableLiveData<Boolean>()
    val hasSchool: LiveData<Boolean>
        get() = _hasSchool

    private var job = Job()
    private  val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    fun hasSchool() {
        coroutineScope.launch {
            initHasSchool()
        }

    }

    private suspend fun initHasSchool() {
        val db = FirebaseFirestore.getInstance()
        val authUser = FirebaseAuth.getInstance().currentUser
        val doc = db.collection("users").document(authUser?.email.toString())

        try {

        }catch (e: FirebaseFirestoreException){ }

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}