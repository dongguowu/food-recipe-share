package com.lduboscq.appkickstarter.xiao_login

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
/**
 * Represents the model for the login screen.
 *
 * @param repository The repository for login operations.
 */
class LoginScreenModel(private val repository: LoginRepositoryRealm) :

StateScreenModel<LoginScreenModel.State>(State.Init)
{

    /**
     * Represents the possible states of the login screen.
     */
    sealed class State {
        object Init : State()
        object Loading : State()
        sealed class Result : State() {
            class SingleResult(val loginUserData: LoginUserData?) : Result()
            class MultipleResult(val loginUserDatas: Flow<LoginUserData>?) : Result()
        }
    }
    sealed class LoginResult {
        object Success : LoginResult()
        object Error : LoginResult()
    }

    /**
     * Performs the login operation.
     *
     * @param email The email address.
     * @param password The password.
     */
    suspend fun login(email: String, password: String):LoginResult {
        return if (isValidCredentials(email, password)) {
            LoginResult.Success
        } else {
            LoginResult.Error
        }
    }
    private fun isValidCredentials(email: String, password: String): Boolean {
        // Replace this logic with your actual validation against the database
        val validEmail = email
        val validPassword = password
        return email == validEmail && password == validPassword
    }
}