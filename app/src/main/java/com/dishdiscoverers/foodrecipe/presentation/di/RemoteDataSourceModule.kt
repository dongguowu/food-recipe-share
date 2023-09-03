package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.data.remote.TheMealAPIService
import com.dishdiscoverers.core.data.repository.dataSource.RemoteRecipeDataSourceImpl
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
    fun provideRecipeRemoteDataSource(api: TheMealAPIService): RemoteRecipeDataSourceImpl {
        return RemoteRecipeDataSourceImpl(api)
    }
}