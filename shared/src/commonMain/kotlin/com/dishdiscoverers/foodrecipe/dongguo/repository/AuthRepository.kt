package com.dishdiscoverers.foodrecipe.dongguo.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow


/**
 * Repository class for handling authentication operations.
 */
class AuthRepository {

    val currentUser: FirebaseUser? = null
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    /**
     * Creates a new user account with the specified email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A resource containing the created user if successful, or an exception if failed.
     */
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result =
                Firebase.auth.createUserWithEmailAndPassword(email = email, password = password)
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    /**
     * Signs in the user with the specified email and password.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A resource containing the signed-in user if successful, or an exception if failed.
     */
    suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
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

    /**
     * Updates the password for the current user.
     *
     * @param newPassword The new password to set for the user.
     * @return A resource indicating the result of the password update operation.
     */
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


