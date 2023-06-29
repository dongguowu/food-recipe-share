package com.dishdiscoverers.foodrecipe.dongguo

data class FirebaseUser(var email: String, var password: String)

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}


class AuthRepositoryImpl : AuthRepository {
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
            return FirebaseUser("dongguo@wu.com", "dongguo")
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
        return Resource.Success(FirebaseUser("dongguo@wu.com", "dongguo"))
    }

    override fun logout() {
        _currentUser = null
    }

}