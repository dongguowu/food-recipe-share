package com.dishdiscoverers.foodrecipe.data

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.model.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.model.RecipeRepository

import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val localRepository: RecipeRepository,
) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<Recipe>) : State()
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
                State.Result(list = localRepository.searchRecipesByTitle(title))
        }
    }

    fun searchRecipeByIngredient(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(list = localRepository.searchRecipesByIngredient(title))
        }
    }

}