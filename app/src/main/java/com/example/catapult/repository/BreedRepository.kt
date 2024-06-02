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

    fun guessTheFactFetch(): List<QuizQuestion> {
        val allBreeds = allBreeds()
        if (allBreeds.isEmpty()) {
            throw Exception("No breeds available for the game")
        }
        val questions = mutableListOf<QuizQuestion>()

        // Question Type 2: Guess the outlier temperament
        val outlierQuestionBreed = allBreeds.random()
        val outlierQuestionImage = allImagesForBreed(outlierQuestionBreed.id).random().asViewBreedImage()
        val outlierTemperaments = outlierQuestionBreed.temperament.shuffled().take(3)
        if (outlierTemperaments.isEmpty()) {
            throw Exception("No temperaments available for the outlier question breed")
        }
        val otherTemperaments = allBreeds.filter { it != outlierQuestionBreed }.flatMap { it.temperament }.distinct()
        if (otherTemperaments.isEmpty()) {
            throw Exception("No other temperaments available for the outlier question")
        }
        val outlierQuestionOptions = (outlierTemperaments + otherTemperaments.first()).shuffled()
        val outlierQuestionCorrectAnswer = outlierQuestionOptions.indexOfFirst { !outlierQuestionBreed.temperament.contains(it) }
        questions.add(QuizQuestion(QuestionType.GUESS_THE_OUTLIER_TEMPERAMENT, Pair(outlierQuestionBreed, outlierQuestionImage), outlierQuestionOptions, outlierQuestionCorrectAnswer))

        // Question Type 3: Guess the correct temperament
        val temperamentQuestionBreed = allBreeds.random()
        val temperamentQuestionImage = allImagesForBreed(temperamentQuestionBreed.id).random().asViewBreedImage()
        val correctTemperament = temperamentQuestionBreed.temperament.random()
        val otherTemperamentsForCorrect = allBreeds.flatMap { it.temperament }.distinct().filter { it != correctTemperament }.shuffled().take(3)
        if (otherTemperamentsForCorrect.isEmpty()) {
            throw Exception("No other temperaments available for the correct temperament question")
        }
        val temperamentQuestionOptions = (otherTemperamentsForCorrect + correctTemperament).shuffled()
        val temperamentQuestionCorrectAnswer = temperamentQuestionOptions.indexOf(correctTemperament)
        questions.add(QuizQuestion(QuestionType.GUESS_THE_CORRECT_TEMPERAMENT, Pair(temperamentQuestionBreed, temperamentQuestionImage), temperamentQuestionOptions, temperamentQuestionCorrectAnswer))

        return questions
    }

    enum class QuestionType {
        GUESS_THE_BREED,
        GUESS_THE_OUTLIER_TEMPERAMENT,
        GUESS_THE_CORRECT_TEMPERAMENT
    }

    data class QuizQuestion(
        val questionType: QuestionType,
        val breedAndImage: Pair<UIBreed, UIBreedImage>,
        val options: List<String>,
        val correctAnswer: Int
    )


}