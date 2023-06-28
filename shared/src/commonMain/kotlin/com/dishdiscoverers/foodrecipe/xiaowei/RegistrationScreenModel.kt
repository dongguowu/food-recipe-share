package com.dishdiscoverers.foodrecipe.xiaowei

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
/**
 * Screen model class for the user registration screen.
 *
 * @property repository The repository used for data operations.
 */
class RegistrationScreenModel(private val repository: RegisterRepositoryRealm) :

    StateScreenModel<RegistrationScreenModel.State>(State.Init)
    {

        /**
         * Sealed class representing the different states of the screen model.
         */
        sealed class State {
            object Init : State()
            object Loading : State()
            sealed class Result : State() {
                class SingleResult(val userData: UserData?) : Result()
                class MultipleResult(val userDatas: Flow<UserData>?) : Result()
            }
        }

        /**
         * Function to retrieve user data based on the provided parameters.
         *
         * @param userName The username of the user.
         * @param email The email address of the user.
         * @param password The password of the user.
         * @param confirmPassword The confirmed password of the user.
         */
        fun getUser(userName: String, email: String, password: String, confirmPassword: String) {
            coroutineScope.launch {
                mutableState.value = State.Loading
                mutableState.value = State.Result.SingleResult(
                    repository.getUser(
                        userName,
                        email,
                        password,
                        confirmPassword
                    )
                )
            }
        }
        /**
         * Function to add a new user.
         *
         * @param userName The username of the user.
         * @param email The email address of the user.
         * @param password The password of the user.
         * @param confirmPassword The confirmed password of the user.
         */

        fun addUser(userName: String, email: String, password: String, confirmPassword: String) {
            coroutineScope.launch {
                mutableState.value = State.Loading
                val newUser = UserData(
                    username = userName,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    user = null
                )
                mutableState.value = State.Result.SingleResult(repository.addUser(newUser))
            }
        }
        /**
         * Function to delete a user.
         *
         * @param userName The username of the user to delete.
         */
        fun deleteUser(userName: String) {
            coroutineScope.launch {
                mutableState.value = State.Loading

                mutableState.value = State.Result.SingleResult(
                    repository.deleteOneUser(userName)
                )
            }
        }
        /**
         * Function to update the password of a user.
         *
         * @param userName The username of the user.
         * @param password The new password.
         * @param confirmPassword The confirmed new password.
         */
        fun updatePassword(userName: String, password: String, confirmPassword: String) {
            coroutineScope.launch {
                mutableState.value = State.Loading

                mutableState.value = State.Result.SingleResult(
                    repository.updateUser(
                        userName,
                        password,
                        confirmPassword
                    )
                )
            }
        }

}