package com.example.catapult.api

import com.example.catapult.api.models.BreedApiModel
import com.example.catapult.api.models.BreedImageApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(@Query("limit") limit: Int = 200): List<BreedApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: Int,
    ): BreedApiModel

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") imageId: Int,
    ): List<BreedApiModel>

    @GET("images/search")
    suspend fun getImages(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10
    ): List<BreedImageApiModel>

}