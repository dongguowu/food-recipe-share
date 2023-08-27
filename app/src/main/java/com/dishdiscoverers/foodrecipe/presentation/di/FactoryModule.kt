package com.dishdiscoverers.foodrecipe.presentation.di

import android.app.Application
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase
import com.dishdiscoverers.foodrecipe.framework.RecipeViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Singleton
    @Provides
    fun provideRecipeViewModelFactory(
        app: Application,
        getSearchedRecipesUseCase: GetSearchedRecipesUseCase
    ): RecipeViewModelFactory{
        return RecipeViewModelFactory(app, getSearchedRecipesUseCase)
    }
}