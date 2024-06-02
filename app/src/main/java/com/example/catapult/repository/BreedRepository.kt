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
import com.example.catapult.model.quiz.guess_fact.QuestionType
import com.example.catapult.model.quiz.guess_fact.QuizQuestion
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
    // TODO: FIX BUG - EMPTY COLLECTION [PRIORITY: HIGH]
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

    // TODO: REFACTOR [PRIORITY: HIGH]
    suspend fun guessTheFactFetch(): List<QuizQuestion> {
        val allBreeds = allBreeds()
        if (allBreeds.isEmpty()) {
            throw Exception("No breeds available for the game")
        }
        val questions = mutableListOf<QuizQuestion>()
        // Question Type 1: Guess the breed name
        questions.add(generateGuessTheBreedNameQuestion(allBreeds))

        // Question Type 2: Guess the outlier temperament
        questions.add(generateOutlierTemperamentQuestion(allBreeds))

        // Question Type 3: Guess the correct temperament
        questions.add(generateCorrectTemperamentQuestion(allBreeds))

        return questions
    }

    private suspend fun generateGuessTheBreedNameQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        if (allBreeds.isEmpty()) {
            fetchAllBreeds()
            return generateGuessTheBreedNameQuestion(allBreeds())
        }

        val breedQuestionBreed = allBreeds.random()
        val images = allImagesForBreed(breedQuestionBreed.id)
        if (images.isEmpty()) {
            fetchBreedImages(breedQuestionBreed.id)
            return generateGuessTheBreedNameQuestion(allBreeds())
        }

        val breedQuestionImage = images.random().asViewBreedImage()
        val otherBreedsForBreedQuestion = allBreeds.filter { it != breedQuestionBreed }.shuffled().take(3)
        if (otherBreedsForBreedQuestion.size < 3) {
            throw Exception("Not enough other breeds for the breed question")
        }

        val breedQuestionOptions = (otherBreedsForBreedQuestion.map { it.name } + breedQuestionBreed.name).shuffled()
        val breedQuestionCorrectAnswer = breedQuestionOptions.indexOf(breedQuestionBreed.name)
        return QuizQuestion(QuestionType.GUESS_THE_BREED, Pair(breedQuestionBreed, breedQuestionImage), breedQuestionOptions, breedQuestionCorrectAnswer)
    }

    private suspend fun generateOutlierTemperamentQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        if (allBreeds.isEmpty()) {
            fetchAllBreeds()
            return generateOutlierTemperamentQuestion(allBreeds())
        }

        val outlierQuestionBreed = allBreeds.random()
        val images = allImagesForBreed(outlierQuestionBreed.id)
        if (images.isEmpty()) {
            fetchBreedImages(outlierQuestionBreed.id)
            return generateOutlierTemperamentQuestion(allBreeds())
        }

        val outlierQuestionImage = images.random().asViewBreedImage()
        val outlierTemperaments = outlierQuestionBreed.temperament.shuffled().take(3)
        if (outlierTemperaments.isEmpty()) {
            throw Exception("No temperaments available for the outlier question breed")
        }

        val otherTemperaments = allBreeds
            .filter { it != outlierQuestionBreed }
            .flatMap { it.temperament }.distinct()
        if (otherTemperaments.isEmpty()) {
            throw Exception("No other temperaments available for the outlier question")
        }

        val outlierQuestionOptions = (outlierTemperaments + otherTemperaments.first()).shuffled()
        val outlierQuestionCorrectAnswer = outlierQuestionOptions.indexOfFirst { !outlierQuestionBreed.temperament.contains(it) }
        return QuizQuestion(QuestionType.GUESS_THE_OUTLIER_TEMPERAMENT, Pair(outlierQuestionBreed, outlierQuestionImage), outlierQuestionOptions, outlierQuestionCorrectAnswer)
    }

    private suspend fun generateCorrectTemperamentQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        if (allBreeds.isEmpty()) {
            fetchAllBreeds()
            return generateCorrectTemperamentQuestion(allBreeds())
        }

        val temperamentQuestionBreed = allBreeds.random()
        val images = allImagesForBreed(temperamentQuestionBreed.id)
        if (images.isEmpty()) {
            fetchBreedImages(temperamentQuestionBreed.id)
            return generateCorrectTemperamentQuestion(allBreeds())
        }

        val temperamentQuestionImage = images.random().asViewBreedImage()
        val correctTemperament = temperamentQuestionBreed.temperament.random()
        val otherTemperamentsForCorrect = allBreeds.flatMap { it.temperament }.distinct().filter { it != correctTemperament }.shuffled().take(3)
        if (otherTemperamentsForCorrect.isEmpty()) {
            throw Exception("No other temperaments available for the correct temperament question")
        }
        val temperamentQuestionOptions = (otherTemperamentsForCorrect + correctTemperament).shuffled()
        val temperamentQuestionCorrectAnswer = temperamentQuestionOptions.indexOf(correctTemperament)
        return QuizQuestion(QuestionType.GUESS_THE_CORRECT_TEMPERAMENT, Pair(temperamentQuestionBreed, temperamentQuestionImage), temperamentQuestionOptions, temperamentQuestionCorrectAnswer)
    }

}