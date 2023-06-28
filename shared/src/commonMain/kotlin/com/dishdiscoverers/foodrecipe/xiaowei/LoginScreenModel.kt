package com.dishdiscoverers.foodrecipe.xiaowei

import cafe.adriel.voyager.core.model.StateScreenModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Represents the model for the login screen.
 *
 * @param repository The repository for login operations.
 */
class LoginScreenModel(private val repository: LoginRepositoryRealm) :

    StateScreenModel<LoginScreenModel.State>(State.Init) {

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
    suspend fun login(email: String, password: String): LoginResult {
        return if (isValidCredentials(email, password)) {
            LoginResult.Success
        } else {
            LoginResult.Error
        }
    }

    @OptIn(InternalAPI::class)
    private suspend fun isValidCredentials(email: String, password: String): Boolean {
        // Replace this logic with your actual validation against the database
//        val validEmail = email
//        val validPassword = password
//        return email == validEmail && password == validPassword
        // Dongguo version
        val ktorClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }

        @Serializable
        data class UserLogin(val email: String, val password: String)

        val urlString = "https://map07-group03-test.vercel.app/api/users/login"
        runBlocking {

            var response: HttpResponse = ktorClient.post(urlString) {
                contentType(ContentType.Application.Json)
                setBody(
                    UserLogin(
                        email = email,
                        password = password,
                    )
                )
            }
            println("*********************************************")
            println(response.bodyAsText())
            println("*********************************************")
        }
        return true
    }
}