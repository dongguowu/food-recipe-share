package com.dishdiscoverers.foodrecipe.xiaowei

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey


class User : RealmObject {
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""
}

data class UserData(
    var id: String? = null,
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var listOfUsers : ArrayList<User>? = null,
    var user: User?
)