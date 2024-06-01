package com.example.catapult.repository

import android.util.Log
import com.example.catapult.api.BreedsApi
import com.example.catapult.api.networking.retrofit
import com.example.catapult.database.CatapultDatabase
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.mappers.asBreedDbModel
import com.example.catapult.model.mappers.asBreedImageDbModel
import com.example.catapult.model.mappers.asViewBreed
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object BreedRepository {

    // Attributes
    private val database by lazy { CatapultDatabase.database }
    private val breedsApi: BreedsApi by lazy { retrofit.create(BreedsApi::class.java) }

    // Methods
    suspend fun fetchAllBreeds() = coroutineScope {
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertAll(breeds.map { it.asBreedDbModel() })

        breeds.map { breed ->
            async {
                try {
                    fetchBreedImages(breed.id)
                } catch (e: Exception) {
                    Log.e("BreedRepository", "Failed to fetch images for breed ${breed.id}, error: ${e.message}")
                }
            }
        }.forEach { it.await() }
    }

    private suspend fun fetchBreedImages(breedId: String) {
        val breedImages = breedsApi.getImages(breedId)
        breedImages.forEach { it.breedId = breedId }

        database.breedImageDao().insertAll(breedImages.map { it.asBreedImageDbModel() })
    }

    fun observeAllBreeds() = database.breedDao().observeAll()

    fun fetchBreedDetails(breedId: String): UIBreed? {
        return database.breedDao().getBreedById(breedId)?.asViewBreed()
    }

    // Getters
    fun allBreeds() : List<UIBreed> = database.breedDao().getAll().map { it.asViewBreed() }

    fun allImagesForBreed(breedId: String) = database.breedImageDao().getAllImagesForBreed(breedId)

    fun getImageById(id: String) = database.breedImageDao().getImageById(id)

    fun getById(id: String) : UIBreed? {
        return database.breedDao().getBreedById(id)?.asViewBreed()
    }

}