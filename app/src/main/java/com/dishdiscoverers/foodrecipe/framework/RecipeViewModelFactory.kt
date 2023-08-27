package com.dishdiscoverers.foodrecipe.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase

class RecipeViewModelFactory(
    private val app:Application,
    private val getSearchedRecipesUseCase: GetSearchedRecipesUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(app, getSearchedRecipesUseCase) as T
    }
}