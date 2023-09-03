package com.dishdiscoverers.foodrecipe.presentation.di

import com.dishdiscoverers.core.common.Constants
import com.dishdiscoverers.core.data.remote.TheMealAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
//    @Singleton
//    @Provides
//    fun provideRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    @Singleton
    @Provides
    fun provideTheMealAPIService(): TheMealAPIService {
        return Retrofit
            .Builder()
            .baseUrl(Constants.THE_MEAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealAPIService::class.java)
    }
}