package com.lduboscq.appkickstarter.xiao_login

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
/**
 * Represents a login user stored in Realm.
 */
class LoginUser: RealmObject {
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()
    var email: String = ""
    var password: String = ""

}
/**
 * Represents login user data.
 *
 * @param id The ID of the user.
 * @param email The email address of the user.
 * @param password The password of the user.
 * @param user The user associated with the login data.
 */
data class LoginUserData(
    var id: String? = null,
    var email: String = "",
    var password: String = "",
    var user: User?
)