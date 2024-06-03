package com.example.catapult.repository

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.api.BreedsApi
import com.example.catapult.api.networking.retrofit
import com.example.catapult.database.CatapultDatabase
import com.example.catapult.model.quiz.SecondQuizQuestion
import com.example.catapult.repository.fetchers.BreedFetcher
import com.example.catapult.repository.fetchers.QuizGenerator

object BreedRepository {

    private val database by lazy { CatapultDatabase.database }
    private val breedsApi: BreedsApi by lazy { retrofit.create(BreedsApi::class.java) }
    private val breedFetcher = BreedFetcher(database, breedsApi)
    private val quizGenerator = QuizGenerator(breedFetcher)

    suspend fun fetchAllBreeds() = breedFetcher.fetchAllBreeds()

    fun observeAllBreeds() = database.breedDao().observeAll()
    fun fetchBreedDetails(breedId: String): UIBreed? = breedFetcher.fetchBreedDetails(breedId)
    fun allBreeds(): List<UIBreed> = breedFetcher.allBreeds()
    fun allImagesForBreed(breedId: String) = breedFetcher.allImagesForBreed(breedId)
    fun getImageById(id: String) = breedFetcher.getImageById(id)
    fun getById(id: String): UIBreed? = breedFetcher.getById(id)

    // Quiz Getters
    fun guessTheCatFetch(): List<Pair<UIBreed, UIBreedImage>> = quizGenerator.guessTheCatFetch()
    suspend fun guessTheFactFetch(): List<SecondQuizQuestion> = quizGenerator.guessTheFactFetch()

}
