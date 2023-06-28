package com.dishdiscoverers.foodrecipe.z_showList

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.dishdiscoverers.foodrecipe.dongguo.Category
import com.dishdiscoverers.foodrecipe.dongguo.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeScreenModel(
    private val localRepository: RecipeRepository,
    private val secondLocalRepository: RecipeRepository,
    private val apiRepository: RecipeRepository,
) :
    StateScreenModel<RecipeScreenModel.State>(State.Init) {

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<Recipe>) : State()
    }

    private val _categories = MutableStateFlow<Resource<List<Category>>?>(null)
    val categories: StateFlow<Resource<List<Category>>?> = _categories

    init {
        coroutineScope.launch {
            _categories.value = Resource.Loading
            _categories.value = apiRepository.getAllCategory()
        }
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

    override fun onDispose() {
        println("ScreenModel: dispose ")
    }
}
