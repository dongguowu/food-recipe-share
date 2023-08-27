package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.data.model.meal.TheMealRecipe
import com.dishdiscoverers.core.data.repository.RecipeRepositoryDateSourceImpl
import com.dishdiscoverers.core.data.repository.dataSourceImpl.RemoteRecipeDataSource
import com.dishdiscoverers.core.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRecipeRepository(dataSource: RemoteRecipeDataSource): RecipeRepository{
        return  RecipeRepositoryDateSourceImpl(dataSource)

    }
}