package com.dishdiscoverers.core.domain.repository

import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.domain.model.LoggedInUser

interface AuthenticatorInterface {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<LoggedInUser>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}