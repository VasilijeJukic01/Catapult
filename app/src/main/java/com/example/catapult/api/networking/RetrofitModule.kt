package com.example.catapult.api.networking

import com.example.catapult.api.BreedsApi
import com.example.catapult.api.LeaderboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedRetrofitModule {

    @Provides
    @Singleton
    fun provideBreedRetrofit(@Named("BreedApiRetrofit") retrofit: Retrofit): BreedsApi = retrofit.create()

}

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardRetrofitModule {

    @Provides
    @Singleton
    fun provideBreedRetrofit(@Named("LeaderboardApiRetrofit") retrofit: Retrofit): LeaderboardApi = retrofit.create()

}