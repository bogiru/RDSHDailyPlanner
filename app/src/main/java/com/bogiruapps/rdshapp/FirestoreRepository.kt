package com.bogiruapps.rdshapp

import android.app.Application
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
        val document = db.collection("users").document(userId).get().await()
        return document["school"].toString()
    }

    suspend fun getSchools(): List<String> {
        val documents = db.collection("schools").get().await()
        val schools = mutableListOf<String>()
        for (document in documents) {
            schools.add(document["name"].toString())
        }
        return schools
    }

}