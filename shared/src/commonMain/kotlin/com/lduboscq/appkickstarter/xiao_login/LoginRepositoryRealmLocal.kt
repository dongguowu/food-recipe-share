package com.lduboscq.appkickstarter.xiao_login

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
/**
 * A local implementation of the login repository using Realm.
 */
class LoginRepositoryRealmLocal: LoginRepositoryRealm() {
    override suspend fun setupRealmSync() {

            val config = RealmConfiguration.Builder(setOf(User::class))
                .build()
            realm = Realm.open(config)

    }

    override suspend fun getUser(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): UserData? {
        TODO("Not yet implemented")
    }
}