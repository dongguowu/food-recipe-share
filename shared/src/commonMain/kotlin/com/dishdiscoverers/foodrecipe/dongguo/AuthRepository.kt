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

interface AuthRepositoryInterface {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}


class AuthRepositoryImpl : AuthRepositoryInterface {
    private var _currentUser: FirebaseUser? = null

    override val currentUser: FirebaseUser?
        get() = _currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val user = signInWithEmailAndPassword(email, password)
            if (user == null) {
                Resource.Failure(Exception("password is invalidate"))
            } else {
                _currentUser = user
                Resource.Success(user!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        if (password == "dongguo") {
//            return FirebaseUser("dongguo@wu.com", "dongguo")
        }
        return null
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            result.user?.updateProfile(
//                UserProfileChangeRequest.Builder().setDisplayName(name).build()
//            )?.await()
//            return Resource.Success(result.user!!)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Failure(e)
//        }
//        return Resource.Success(FirebaseUser("dongguo@wu.com", "dongguo"))
        return Resource.Failure(Exception("dongguo@wu.com"))
    }

    override fun logout() {
        _currentUser = null
    }

}