package com.dishdiscoverers.foodrecipe.framework

import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.data.repository.dataSource.AuthenticatorDataSourceInterface
import com.dishdiscoverers.core.domain.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FriebaseDataSource : AuthenticatorDataSourceInterface {
    private lateinit var auth: FirebaseAuth

    override suspend fun login(email: String, password: String): Resource<LoggedInUser> {
        try {
            val response = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = response.user
            if (firebaseUser != null) {
                val loggedInUser = LoggedInUser(
                    firebaseUser.uid,
                    firebaseUser.email ?: ""
                )
                return Resource.Success(loggedInUser)
            } else {
                return Resource.Failure(java.lang.Exception("Login failed: User not found"))
            }
        } catch (e: Exception) {
            return Resource.Failure(e)
        }

    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}