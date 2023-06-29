package com.dishdiscoverers.foodrecipe.xiaowei

import com.dishdiscoverers.foodrecipe.xiaowei.LoginUserData
import com.dishdiscoverers.foodrecipe.xiaowei.UserData

interface UserRepository {
    suspend fun getUser(
        userName: String,
        email: String,
    ): LoginUserData?

    suspend fun getUser(      userName: String,
                              email: String,
                              password: String,
                              confirmPassword: String): UserData?
    suspend fun getUserByUsername(username: String): UserData?
    suspend fun addUser(userData: UserData): UserData?

    suspend fun deleteOneUser(userName: String): UserData?

    suspend fun updateUser(userName: String, password: String, confirmPassword: String): UserData?

    suspend fun getAllUsers(): List<UserData>?

}