package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.data.api.TheMealAPIService
import com.dishdiscoverers.core.data.repository.dataSourceImpl.RemoteRecipeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Singleton
    @Provides
    fun provideRecipeRemoteDataSource(theMealAPIService: TheMealAPIService): RemoteRecipeDataSource {
        return RemoteRecipeDataSource(theMealAPIService)
    }
}