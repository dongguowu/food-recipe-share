package com.dishdiscoverers.foodrecipe.dongguo.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Category
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserFavoriteRecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeComment
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val apiRepository: RecipeRepository,
    private val authRepository: AuthRepository,
    private val commentRepository: UserRecipeCommentRepository,
    private val favoriteRepository: UserFavoriteRecipeRepository,

) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<Recipe>) : State()
    }

    // Auth
    val currentUser: FirebaseUser? get() = authRepository.currentUser
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow


    // Category
    private val _categories = MutableStateFlow<Resource<List<Category>>?>(null)
    val categories: StateFlow<Resource<List<Category>>?> = _categories

    //  Comment
    private val _comments = MutableStateFlow<Resource<List<UserRecipeComment>>?>(null)
    val comments: StateFlow<Resource<List<UserRecipeComment>>?> = _comments
    fun getComments(recipeId: String) = coroutineScope.launch {
        _comments.value = Resource.Loading
        val result = commentRepository.getCommentsByRecipeId(recipeId)
        _comments.value = result
    }

    private val _comment = MutableStateFlow<Resource<UserRecipeComment>?>(null)
    val comment: StateFlow<Resource<UserRecipeComment>?> = _comment
    fun addComment(
        userId: String,
        recipeId: String,
        text: String,
        imageUrl: String?
    ) = coroutineScope.launch {
        _comment.value = Resource.Loading
        val result = commentRepository.addComment(
            userId = userId,
            recipeId = recipeId,
            text = text,
            imageUrl = null
        )
        _comment.value = result
    }

    // Favorite
    private val _favorite = MutableStateFlow<Resource<Boolean>?>(null)
    val favorite: StateFlow<Resource<Boolean>?> = _favorite
    fun getFavorite(userId: String, recipeId: String) = coroutineScope.launch {
        _favorite.value = Resource.Loading
        val result = favoriteRepository.getFavoritesRecipe(userId = userId, recipeId = recipeId)
        _favorite.value = result
    }

    fun addFavorite(
        userId: String,
        recipeId: String,
    ) = coroutineScope.launch {
        _favorite.value = Resource.Loading
        var result = favoriteRepository.addFavoriteRecipe(
            userId = userId,
            recipeId = recipeId,
        )
        _favorite.value = Resource.Success(true)
    }
    fun deleteFavorite(
        userId: String,
        recipeId: String,
    ) = coroutineScope.launch {
        _favorite.value = Resource.Loading
        var result = favoriteRepository.deleteFavoriteRecipe(
            userId = userId,
            recipeId = recipeId,
        )
        _favorite.value = Resource.Success(true)
    }

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.value = Resource.Success(authRepository.currentUser)
        }

        // Category
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
                State.Result(list = apiRepository.getAllRecipe())
        }
    }


    fun searchRecipe(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = apiRepository.searchRecipesByTitle(title))
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
                State.Result(list = apiRepository.searchRecipesByIngredient(title))
        }
    }

    override fun onDispose() {
        println("ScreenModel: dispose ")
    }
}
