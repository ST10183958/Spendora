package com.menak.login.data.Repository

import com.menak.login.data.Dao.UserDao
import com.menak.login.data.Entity.UserEntity
import com.menak.login.data.firebase.FirebaseAuthRepository
import com.menak.login.data.firebase.FirestoreRepository

class AuthRepository(
    private val firebase: FirebaseAuthRepository,
    private val firestore: FirestoreRepository
) {

    suspend fun register(username: String, password: String): Result<String> {
        return firebase.register(username, password).onSuccess {
            firestore.createUserProfile(username)
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return firebase.login(username, password)
    }

    fun logout() = firebase.logout()
}