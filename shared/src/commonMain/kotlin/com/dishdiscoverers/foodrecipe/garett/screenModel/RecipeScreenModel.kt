package com.lduboscq.appkickstarter.main.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.lduboscq.appkickstarter.main.data.Ingredient
import com.lduboscq.appkickstarter.main.data.Recipe
import com.lduboscq.appkickstarter.main.data.RecipeRepository

import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val localRepository: com.dishdiscoverers.foodrecipe.dongguo.RecipeRepository,
    private val secondLocalRepository: com.dishdiscoverers.foodrecipe.dongguo.RecipeRepository,
    private val apiRepository: com.dishdiscoverers.foodrecipe.dongguo.RecipeRepository,
) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<com.dishdiscoverers.foodrecipe.dongguo.Recipe>) : State()
    }

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
                State.Result(list = secondLocalRepository.searchRecipesByIngredient(title))
        }
    }

//    suspend fun searchRecipe(title: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value =
//                State.Result(recipeList = repository.searchRecipesByTitle(title))
//        }
//    }
//
//    suspend fun findRecipeById(id: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value =
//                State.Result(recipe = repository.findRecipeById(id))
//        }
//    }
}