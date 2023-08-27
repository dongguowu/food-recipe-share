package com.dishdiscoverers.foodrecipe.framework

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.model.UserRecipes
import com.dishdiscoverers.core.data.utility.Resource
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeViewModel(
    private val app: Application,
    private val getSearchedRecipesUseCase: GetSearchedRecipesUseCase
) : AndroidViewModel(app) {
    private val recipesList: MutableLiveData<Resource<List<FoodRecipe>>> = MutableLiveData()

    fun getResearchedRecipes(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        recipesList.postValue(Resource.Loading)
        try {
            if (isInternetAvailable(app)) {
                val results = getSearchedRecipesUseCase.execute(searchQuery)
                recipesList.postValue(results)
            } else {
                recipesList.postValue(Resource.Failure(Exception("Internet is not available")))
            }
        } catch (ex: Exception) {
            recipesList.postValue(Resource.Failure(ex))
        }
    }

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