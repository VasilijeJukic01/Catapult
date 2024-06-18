package com.example.catapult.repository

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.api.BreedsApi
import com.example.catapult.database.AppDatabase
import com.example.catapult.debug.ErrorTracker
import com.example.catapult.model.quiz.GuessCatQuestion
import com.example.catapult.model.quiz.GuessFactQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestion
import com.example.catapult.repository.fetchers.BreedFetcher
import com.example.catapult.repository.fetchers.QuizGenerator
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase,
    private val errorTracker: ErrorTracker
) {

    private val breedFetcher = BreedFetcher(database, breedsApi)
    private val quizGenerator = QuizGenerator(breedFetcher, errorTracker)

    suspend fun fetchAllBreeds() = breedFetcher.fetchAllBreeds()

    fun observeAllBreeds() = database.breedDao().observeAll()
    fun fetchBreedDetails(breedId: String): UIBreed? = breedFetcher.fetchBreedDetails(breedId)
    fun allBreeds(): List<UIBreed> = breedFetcher.allBreeds()
    fun allImagesForBreed(breedId: String) = breedFetcher.allImagesForBreed(breedId)
    fun getImageById(id: String) = breedFetcher.getImageById(id)
    fun getById(id: String): UIBreed? = breedFetcher.getById(id)

    // Quiz Getters
    suspend fun guessTheCatFetch(): List<GuessCatQuestion> = quizGenerator.guessTheCatFetch()
    suspend fun guessTheFactFetch(): List<GuessFactQuestion> = quizGenerator.guessTheFactFetch()
    suspend fun leftOrRightFetch(): List<LeftOrRightQuestion> = quizGenerator.leftOrRightFetch()

}
