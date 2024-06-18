package com.example.catapult.repository.fetchers

import android.util.Log
import com.example.catapult.api.BreedsApi
import com.example.catapult.database.AppDatabase
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.mappers.asBreedDbModel
import com.example.catapult.model.mappers.asBreedImageDbModel
import com.example.catapult.model.mappers.asViewBreed
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class BreedFetcher(
    private val database: AppDatabase,
    private val breedsApi: BreedsApi
) {

    // Core
    suspend fun fetchAllBreeds() = coroutineScope {
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertAll(breeds.map { it.asBreedDbModel() })

        breeds.map { breed ->
            async {
                fetchBreedImagesSafe(breed.id)
            }
        }.forEach { it.await() }
    }

    suspend fun fetchBreedImagesSafe(breedId: String) {
        try {
            fetchBreedImages(breedId)
        } catch (e: Exception) {
            Log.e("BreedFetcher", "Failed to fetch images for breed $breedId, error: ${e.message}")
        }
    }

    private suspend fun fetchBreedImages(breedId: String) {
        val breedImages = breedsApi.getImages(breedId).onEach { it.breedId = breedId }
        database.breedImageDao().insertAll(breedImages.map { it.asBreedImageDbModel() })
    }

    // Database Operations
    fun fetchBreedDetails(breedId: String): UIBreed? = database.breedDao().getBreedById(breedId)?.asViewBreed()
    fun allBreeds(): List<UIBreed> = database.breedDao().getAll()
        .filter { allImagesForBreed(it.id).isNotEmpty() }
        .map { it.asViewBreed() }
    fun allImagesForBreed(breedId: String) = database.breedImageDao().getAllImagesForBreed(breedId)
    fun getImageById(id: String) = database.breedImageDao().getImageById(id)
    fun getById(id: String): UIBreed? = database.breedDao().getBreedById(id)?.asViewBreed()
}
