package com.example.catapult.api.networking.url_providers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardApiUrlModule {

    @LeaderboardApiUrl
    @Provides
    fun provideLeaderboardApiUrl(): String = "https://rma.finlab.rs/"

}