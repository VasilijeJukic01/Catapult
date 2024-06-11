package com.example.catapult.api.networking.url_providers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BreedsApiUrlModule {

    @BreedsApiUrl
    @Provides
    fun provideBreedsApiUrl(): String = "https://api.thecatapi.com/v1/"

}