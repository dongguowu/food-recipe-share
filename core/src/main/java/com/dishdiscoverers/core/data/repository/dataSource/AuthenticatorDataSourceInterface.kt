package com.dishdiscoverers.core.data.repository.dataSource

import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.domain.model.LoggedInUser

interface AuthenticatorDataSourceInterface {
    suspend fun login(email: String, password: String): Resource<LoggedInUser>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}