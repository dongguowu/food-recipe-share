package com.dishdiscoverers.foodrecipe.presentation.recipe_list

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishdiscoverers.core.common.Constants
import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RecipeViewModel @Inject constructor(
    private val getSearchedRecipesUseCase: GetSearchedRecipesUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(RecipeListState())
    val state: State<RecipeListState> = _state

    init {
//        savedStateHandle.get<String>(Constants.PARAM_SEARCH_QUERY)?.let { searchQuery ->
//            getSearchedRecipes(searchQuery)
//        }

        getSearchedRecipes("egg")
    }

    private fun getSearchedRecipes(searchQuery: String) {
        getSearchedRecipesUseCase(searchQuery).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = RecipeListState(recipes = result.data)
                }
                is Resource.Failure -> {
                    _state.value = RecipeListState(error = result.exception.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = RecipeListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


//   val recipesList: MutableLiveData<List<FoodRecipe>> = MutableLiveData()
//
//    fun getResearchedRecipes(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
//        try {
//            if (isInternetAvailable(app)) {
//                val response = getSearchedRecipesUseCase.execute(searchQuery)
//                val flowResponse = response as Flow<Resource<List<FoodRecipe>>>
//
//                val results = mutableListOf<FoodRecipe>()
//                flowResponse.collect { resource ->
//                    when (resource) {
//                        is Resource.Success -> results.addAll(resource.data)
//                        else -> {}
//                    }
//                }
//                recipesList.postValue(results)
//            } else {
//            }
//        } catch (ex: Exception) {
//        }
//    }

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }
}