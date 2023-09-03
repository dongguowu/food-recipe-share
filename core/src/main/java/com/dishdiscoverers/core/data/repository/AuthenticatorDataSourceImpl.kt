package com.dishdiscoverers.core.data.repository

import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.data.repository.dataSource.AuthenticatorDataSourceInterface
import com.dishdiscoverers.core.domain.model.LoggedInUser
import com.dishdiscoverers.core.domain.repository.AuthenticatorInterface
import javax.inject.Inject

open class AuthenticatorDataSourceImpl @Inject constructor(private val datasource: AuthenticatorDataSourceInterface):
    AuthenticatorInterface {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<LoggedInUser> {
        return datasource.login(email, password)
    }

    override suspend fun logout() {
        datasource.logout()
    }

    override suspend fun isLoggedIn(): Boolean {
        return datasource.isLoggedIn()
    }
}