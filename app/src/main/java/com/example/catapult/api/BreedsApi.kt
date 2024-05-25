package com.example.catapult.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(@Query("limit") limit: Int = 100): List<BreedApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: Int,
    ): BreedApiModel

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") imageId: Int,
    ): List<BreedApiModel>

}