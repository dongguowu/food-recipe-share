package com.dishdiscoverers.foodrecipe.dongguo

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow

class AuthRepository {

    val currentUser: FirebaseUser? = null

    suspend fun signUp(username: String, password: String): Resource<FirebaseUser> {
//        return Firebase.auth.createUserWithEmailAndPassword(email = username, password = password)
        return try {
            val result =
                Firebase.auth.createUserWithEmailAndPassword(email = username, password = password)
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun signIn(username: String, password: String): Resource<FirebaseUser> {
//        return Firebase.auth.signInWithEmailAndPassword(email = username, password = password)
        return try {
            val result =
                Firebase.auth.signInWithEmailAndPassword(email = username, password = password)
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun signOut() {
        return Firebase.auth.signOut()
    }

    suspend fun isLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    suspend fun authStateChanged(): Flow<FirebaseUser?> {
        return Firebase.auth.authStateChanged
    }

    suspend fun delete() {
        if (Firebase.auth.currentUser != null) {
            Firebase.auth.currentUser!!.delete()
        }
    }
}
