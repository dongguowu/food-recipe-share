package com.dishdiscoverers.foodrecipe.xiaowei

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
/**
 * Local implementation of the RegisterRepositoryRealm class for user registration using a local Realm database.
 */
class RegisterRepositoryRealmLocal: RegisterRepositoryRealm() {
    /**
     * Sets up Realm synchronization for the local database.
     */
    override suspend fun setupRealmSync() {
        val config = RealmConfiguration.Builder(setOf(User::class))
            .build()
        realm = Realm.open(config)
    }
    /**
     * Retrieves a user's data from the local database based on the provided email and password.
     */
    override suspend fun getUser(email: String, password: String): LoginUserData? {
        TODO("Not yet implemented")
    }

}