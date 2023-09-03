package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.data.repository.RecipeRepositoryDateSourceImpl
import com.dishdiscoverers.core.data.repository.dataSource.RemoteRecipeDataSourceImpl
import com.dishdiscoverers.core.domain.repository.RecipeRepository
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
    fun provideRecipeRepository(dataSource: RemoteRecipeDataSourceImpl): RecipeRepository {
        return  RecipeRepositoryDateSourceImpl(dataSource)
    }
}