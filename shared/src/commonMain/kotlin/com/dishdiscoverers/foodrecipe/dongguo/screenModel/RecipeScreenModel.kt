package com.dishdiscoverers.foodrecipe.dongguo.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Category
import com.dishdiscoverers.foodrecipe.dongguo.repository.Ingredient
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserFavoriteRecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeComment
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepository
import dev.gitlive.firebase.auth.FirebaseUser
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 *  Store and manage UI-related data and functions:
 *  auth: login, signup,
 *  a list of [Recipe],
 *  a list of [Category],
 *  food recipe review: [UserRecipeComment] and UserFavoriteRecipe,
 */
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
        data class Result(val recipeList: List<Recipe>) : State()
    }

    // Auth
    val currentUser: FirebaseUser? get() = authRepository.currentUser
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.value = Resource.Success(authRepository.currentUser)
        }

        // Category
        coroutineScope.launch {
            _categories.value = Resource.Loading
            _categories.value = apiRepository.getAllCategory()
        }

        // Ingredient
        coroutineScope.launch {
            _ingredients.value = Resource.Loading
            _ingredients.value = apiRepository.getAllIngredient()
        }
    }

    // Category
    private val _categories = MutableStateFlow<Resource<List<Category>>?>(null)
    val categories: StateFlow<Resource<List<Category>>?> = _categories

    // Ingredients
    private val _ingredients = MutableStateFlow<Resource<List<Ingredient>>?>(null)
    val ingredients: StateFlow<Resource<List<Ingredient>>?> = _ingredients


    // Comment
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

    // Food Recipes
    private val _foodRecipes = MutableStateFlow<Resource<List<Recipe>>?>(null)
    val foodRecipes: StateFlow<Resource<List<Recipe>>?> = _foodRecipes
    fun getFavoriteRecipesByUserId(userId: String) = coroutineScope.launch {
        _foodRecipes.value = Resource.Loading
        when (val userFavorites = favoriteRepository.getFavoritesByUserId(userId)) {
            is Resource.Success -> {
                val ids = userFavorites.result.map { it.recipeId }
                Napier.i { ids.toString() }
                when (val recipes = apiRepository.findRecipesByIds(ids)) {
                    is Resource.Success -> {
                        _foodRecipes.value = recipes
                    }

                    is Resource.Loading -> {
                        Resource.Loading
                    }

                    is Resource.Failure -> {
                        Napier.i { recipes.exception.message.toString() }
                    }
                }
            }

            is Resource.Loading -> {
                Resource.Loading
            }

            is Resource.Failure -> {
                Napier.i { userFavorites.exception.message.toString() }
            }
        }
    }

    fun findRecipesByTitle(title: String) = coroutineScope.launch {
        _foodRecipes.value = Resource.Loading
        when (val recipes = apiRepository.findRecipesByTitle(title)) {
            is Resource.Success -> {
                _foodRecipes.value = recipes
            }

            is Resource.Loading -> {
                Resource.Loading
            }

            is Resource.Failure -> {
                Napier.i { recipes.exception.message.toString() }
            }
        }
    }


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
        if (result == Resource.Success(true)) {
            _favorite.value = Resource.Success(true)
        }
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
        if (result == Resource.Success(true)) {
            _favorite.value = Resource.Success(true)
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
    private val _recipe = MutableStateFlow<Resource<Recipe>?>(null)
    val recipe: StateFlow<Resource<Recipe>?> = _recipe
    fun getRecipeById(recipeId: String) = coroutineScope.launch {
        _recipe.value = Resource.Loading
        val result = apiRepository.findRecipeById(recipeId)
        _recipe.value = result
    }

    // Recipes
    fun searchRecipeByIngredient(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(recipeList = apiRepository.searchRecipesByIngredient(title))
        }
    }

    fun debug() {
//        coroutineScope.launch {
//            var r = apiRepository.getAllIngredient()
//            when (r) {
//                is Resource.Success -> {
//                    Napier.i { "------------------------------${r.result.size}------------------------------------"}
//                    for (i in r.result) {
//                        Napier.i { i.toString() }
//                    }
//                }
//
//                is Resource.Failure -> {
//                    Napier.i { r.exception.message.toString() }
//                }
//
//                else -> {
//                    Napier.i { "error" }
//                }
//            }
//
//        }
    }

    override fun onDispose() {
        println("ScreenModel: dispose ")
    }

}
