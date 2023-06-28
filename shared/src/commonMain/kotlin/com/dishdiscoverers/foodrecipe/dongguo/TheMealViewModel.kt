package com.dishdiscoverers.foodrecipe.dongguo

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TheMealViewModel(private val repository: TheMealRepository) :
    StateScreenModel<TheMealViewModel.State>(State.Init) {
    private val _list = MutableStateFlow<Resource<List<RecipeTheMeal>>?>(null)
    val list: StateFlow<Resource<List<RecipeTheMeal>>?> = _list

    sealed class State {
        object Init : State()
        object Loading : State()
        data class Result(val list: List<RecipeTheMeal>) : State()
    }

    init {
        coroutineScope.launch {
            _list.value = Resource.Loading
            _list.value = repository.getRecipes("steak")
        }
    }
}