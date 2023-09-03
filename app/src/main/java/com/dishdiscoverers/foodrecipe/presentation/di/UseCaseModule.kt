package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.domain.repository.RecipeRepository
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideGetSearchedRecipesUseCase(repository: RecipeRepository): GetSearchedRecipesUseCase{
        return GetSearchedRecipesUseCase(repository)
    }
}