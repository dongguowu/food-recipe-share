package com.dishdiscoverers.foodrecipe.dongguo

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val localRepository: RecipeRepository,
    private val apiRepository: RecipeRepository,
    private val authRepository: AuthRepository
) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<Recipe>) : State()
    }

    // Auth
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser? get() = authRepository.currentUser

    // Category
    private val _categories = MutableStateFlow<Resource<List<Category>>?>(null)
    val categories: StateFlow<Resource<List<Category>>?> = _categories

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
        }

        coroutineScope.launch {
            _categories.value = Resource.Loading
            _categories.value = apiRepository.getAllCategory()
        }
    }

    // Auth
    fun loginUser(email: String, password: String) = coroutineScope.launch {
        _loginFlow.value = Resource.Loading
        val result = authRepository.signIn(email, password)
        _loginFlow.value = result
    }

    fun signupUser(name: String, email: String, password: String) = coroutineScope.launch {
        _signupFlow.value = Resource.Loading
        val result = authRepository.signUp(email, password)
        _signupFlow.value = result
    }

    suspend fun logout() {
        authRepository.signOut()
        _loginFlow.value = null
        _signupFlow.value = null
    }


    // Recipe
    fun getAllRecipe() {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = localRepository.getAllRecipe())
        }
    }


    fun searchRecipe(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = localRepository.searchRecipesByTitle(title))
        }
    }

    fun searchRecipeInternet(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = apiRepository.searchRecipesByTitle(title))
        }
    }

    fun searchRecipeByIngredient(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = localRepository.searchRecipesByIngredient(title))
        }
    }

    override fun onDispose() {
        println("ScreenModel: dispose ")
    }
}