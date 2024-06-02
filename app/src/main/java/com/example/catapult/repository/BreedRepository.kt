package com.example.catapult.repository

import android.util.Log
import com.example.catapult.api.BreedsApi
import com.example.catapult.api.networking.retrofit
import com.example.catapult.database.CatapultDatabase
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.mappers.asBreedDbModel
import com.example.catapult.model.mappers.asBreedImageDbModel
import com.example.catapult.model.mappers.asViewBreed
import com.example.catapult.model.mappers.asViewBreedImage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

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

    // Quiz Getters
    fun guessTheCatFetch(): List<Pair<UIBreed, UIBreedImage>> {
        val allBreeds = allBreeds()
        val allTemperaments = allBreeds.flatMap { it.temperament }.distinct()

        val randomTemperament = allTemperaments.random()

        val matchingBreeds = allBreeds.filter { it.temperament.contains(randomTemperament) }
        val nonMatchingBreeds = allBreeds.filter { !it.temperament.contains(randomTemperament) }

        if (matchingBreeds.isEmpty() || nonMatchingBreeds.size < 3) {
            throw Exception("Not enough breeds for the game")
        }

        val selectedMatchingBreed = matchingBreeds[Random.nextInt(matchingBreeds.size)]
        val selectedNonMatchingBreeds = nonMatchingBreeds.shuffled().take(3)

        val selectedBreeds = (listOf(selectedMatchingBreed) + selectedNonMatchingBreeds).filter { breed ->
            val images = allImagesForBreed(breed.id)
            images.isNotEmpty()
        }

        if (selectedBreeds.size < 4) {
            throw Exception("Not enough breeds with images for the game")
        }

        return selectedBreeds.map { breed ->
            val images = allImagesForBreed(breed.id)
            Pair(breed, images[Random.nextInt(images.size)].asViewBreedImage())
        }
    }

}