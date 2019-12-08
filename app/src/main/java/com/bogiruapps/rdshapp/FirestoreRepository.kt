package com.bogiruapps.rdshapp

import android.app.Application
import android.util.Log
import com.bogiruapps.rdshapp.notice.Notice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository(private val application: Application) {
    companion object {
        private var INSTANCE: FirestoreRepository? = null

        fun getInstance(application: Application): FirestoreRepository {
            return if (INSTANCE == null) {
                FirestoreRepository(application)
            }else {
                INSTANCE as FirestoreRepository
            }
        }
    }

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    suspend fun getSchool(userId: String): String {
        Log.i("QWE", "getSchool")
        val document = db.collection("users").document(userId).get().await()
        return document["school"].toString()
    }

    suspend fun getSchools(): List<String> {
        Log.i("QWE", "getSchools")
        val documents = db.collection("schools").get().await()
        val schools = mutableListOf<String>()
        for (document in documents) {
            schools.add(document["name"].toString())
        }
        return schools
    }

    suspend fun setSchool(userId: String, school: String) {
        val document = db.collection("users").document(userId).update  ("school", school).await()
    }

    suspend fun getNotices(): List<Notice> {
        var school = getSchool(auth.currentUser?.email.toString())

        val schools = db.collection("schools").get().await()
        for (document in schools) if (document["name"].toString() == school) {
            school = document.id
            break
        }

        val documents = db.collection("schools/$school/notices").get().await()
        val notices = mutableListOf<Notice>()

        for (document in documents) {
            notices.add(Notice(document["text"].toString()))
        }
        return notices
    }

}