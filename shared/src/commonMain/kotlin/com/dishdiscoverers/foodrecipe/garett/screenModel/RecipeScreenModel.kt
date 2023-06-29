package com.dishdiscoverers.foodrecipe.garett.screenModel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.garett.data.Ingredient
import com.dishdiscoverers.foodrecipe.garett.data.Recipe
import com.dishdiscoverers.foodrecipe.garett.data.RecipeRepository

import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val repository: RecipeRepository
) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        sealed class Result: State() {
            class RecipeResult(val recipeList: List<Recipe>) : Result()
            class RecipeSingleResult(val recipe: Recipe?) : Result()
            class IngredientResult(val ingredientList: List<Ingredient>) : Result()
        }

    }

    suspend fun getAllIngredient() {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result.IngredientResult(ingredientList = repository.getAllIngredient())
        }
    }

    suspend fun getAllRecipe() {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result.RecipeResult(recipeList = repository.getAllRecipe())
        }
    }

    suspend fun searchRecipe(title: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result.RecipeResult(recipeList = repository.searchRecipesByTitle(title))
        }
    }

    suspend fun findRecipeById(id: String) {
        coroutineScope.launch {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result.RecipeSingleResult(recipe = repository.findRecipeById(id))
        }
    }




}