package com.dishdiscoverers.foodrecipe.dongguo.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    val currentUser: FirebaseUser? = null
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser> {
//        return Firebase.auth.createUserWithEmailAndPassword(email = username, password = password)
        return try {
            val result =
                Firebase.auth.createUserWithEmailAndPassword(email = email, password = password)
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
//        return Firebase.auth.signInWithEmailAndPassword(email = username, password = password)
        return try {
            val result =
                Firebase.auth.signInWithEmailAndPassword(email = email, password = password)
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

    suspend fun updatePassword(newPassword: String): Resource<Unit> {
        return try {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                currentUser.updatePassword(newPassword)
                Resource.Success(Unit)
            } else {
                Resource.Failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}


