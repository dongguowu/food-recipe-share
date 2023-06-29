package com.dishdiscoverers.foodrecipe.xiaowei

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.Resource
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Screen model class for the user registration screen.
 *
 * @property authRepository The repository used for data operations.
 */
class RegistrationScreenModel(private val authRepository: AuthRepository) :
    StateScreenModel<RegistrationScreenModel.State>(State.Init) {
    var userName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("dongguo")
    var confirmPassword by mutableStateOf("dongguo")

    /**
     * Sealed class representing the different states of the screen model.
     */
    sealed class State {
        object Init : State()
        object Loading : State()
        sealed class Result : State() {
            class SingleResult(val userData: Resource<FirebaseUser>?) : Result()
            class MultipleResult(val userDatas: Flow<UserData>?) : Result()
            object InvalidUserName : Result()
            object InvalidEmail : Result()
            object InvalidPassword : Result()
            object PasswordMismatch : Result()
        }
    }

    // Dongguo Version
    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    fun signupUser(email: String, password: String) = coroutineScope.launch {
        _signupFlow.value = Resource.Loading
        val result = authRepository.signUp(email, password)
        _signupFlow.value = result
    }

    suspend fun isUsernameUnique(username: String): Boolean {
        // Implement the logic to check if the username is unique
        // You can query your data source (e.g., database) to check if the username exists
        // Return true if the username is unique, false otherwise

//            val existingUser = repository.getUserByUsername(username)
//            return existingUser == null
        return false
    }

//        private fun isEmailUnique(email: String): Boolean {
//            // Implement the logic to check if the email is unique
//            // You can query your data source (e.g., database) to check if the email exists
//            // Return true if the email is unique, false otherwise
//
//            val existingUser = repository.getUserByEmail(email)
//            return existingUser == null
//        }

    fun isEmailValid(email: String): Boolean {
        // Implement the logic to check if the email is valid
        // You can use regular expressions or other validation techniques to validate the email format
        // Return true if the email is valid, false otherwise

        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return email.matches(emailRegex)
    }

    fun isPasswordValid(password: String): Boolean {
        // Implement the logic to check if the password is valid
        // You can define your password validation rules (e.g., minimum length)
        // Return true if the password is valid, false otherwise

        val minLength = 6
        return password.length >= minLength
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        // Implement the logic to check if the password and confirm password match
        // Compare the password and confirmPassword values
        // Return true if they match, false otherwise

        return password == confirmPassword
    }

    /**
     * Function to retrieve user data based on the provided parameters.
     *
     * @param userName The username of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param confirmPassword The confirmed password of the user.
     */
//    fun getUser(userName: String, email: String, password: String, confirmPassword: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.SingleResult(
//                repository.getUser(
//                    userName,
//                    email,
//                    password,
//                    confirmPassword
//                )
//            )
//        }
//    }

    /**
     * Function to add a new user.
     *
     * @param userName The username of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param confirmPassword The confirmed password of the user.
     */

    suspend fun addUser(
        userName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (!isUsernameUnique(userName)) {
            mutableState.value = State.Result.InvalidUserName
            return
        }

        if (!isEmailValid(email)) {
            mutableState.value = State.Result.InvalidEmail
            return
        }

        if (!isPasswordValid(password)) {
            mutableState.value = State.Result.InvalidPassword
            return
        }

        if (!doPasswordsMatch(password, confirmPassword)) {
            mutableState.value = State.Result.PasswordMismatch
            return
        }

        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value = State.Result.SingleResult(authRepository.signUp(email, password))
        }
    }

    /**
     * Function to delete a user.
     *
     * @param userName The username of the user to delete.
     */
//    fun deleteUser(userName: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//
//            mutableState.value = State.Result.SingleResult(
//                repository.deleteOneUser(userName)
//            )
//        }
//    }

    /**
     * Function to update the password of a user.
     *
     * @param userName The username of the user.
     * @param password The new password.
     * @param confirmPassword The confirmed new password.
     */
//    fun updatePassword(userName: String, password: String, confirmPassword: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//
//            mutableState.value = State.Result.SingleResult(
//                repository.updateUser(
//                    userName,
//                    password,
//                    confirmPassword
//                )
//            )
//        }
//    }

}