package com.dishdiscoverers.foodrecipe.xiaowei

import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
/**
 * ViewModel class for the profile screen.
 *
 * @param repository The repository for login-related operations.
 */
class ProfileScreenModel(private val repository: LoginRepositoryRealm) :

    StateScreenModel<ProfileScreenModel.State>(State.Init)
{
    /**
     * Represents the various states of the profile screen.
     */
    sealed class State {
        object Init : State()
        object Loading : State()
        sealed class Result : State() {

            /**
             * Represents the result of a single user data retrieval.
             *
             * @param loginUserData The retrieved login user data.
             */
            class SingleResult(val loginUserData: LoginUserData?) : Result()

            /**
             * Represents the result of multiple user data retrieval.
             *
             * @param loginUserDatas The flow of login user data.
             */
            class MultipleResult(val loginUserDatas: Flow<LoginUserData>?) : Result()
        }
    }


    /**
     * Retrieves the user name for the given email and password.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * todo: have not perform yet
     */
    fun getUserName(email:String,password:String){
        coroutineScope.launch {
            mutableState.value =State.Loading
            val loginUserData = repository.getUser(email, password)
            mutableState.value = State.Result.SingleResult(loginUserData)
        }
    }
}