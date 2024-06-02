package com.example.catapult.repository.fetchers

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.mappers.asViewBreedImage
import com.example.catapult.model.quiz.guess_fact.QuestionType
import com.example.catapult.model.quiz.guess_fact.QuizQuestion
import kotlinx.coroutines.runBlocking

class QuizGenerator(private val breedFetcher: BreedFetcher) {

    // Quiz Category I
    fun guessTheCatFetch(): List<Pair<UIBreed, UIBreedImage>> {
        val allBreeds = breedFetcher.allBreeds()
        val allTemperaments = allBreeds.flatMap { it.temperament }.distinct()
        val randomTemperament = allTemperaments.random()

        val matchingBreeds = allBreeds.filter { it.temperament.contains(randomTemperament) }
        val nonMatchingBreeds = allBreeds.filter { !it.temperament.contains(randomTemperament) }

        if (matchingBreeds.isEmpty() || nonMatchingBreeds.size < 3) {
            throw Exception("Not enough breeds for the game")
        }

        val selectedBreeds = (listOf(matchingBreeds.random()) + nonMatchingBreeds.shuffled().take(3)).filter {
            breedFetcher.allImagesForBreed(it.id).isNotEmpty()
        }

        if (selectedBreeds.size < 4) {
            throw Exception("Not enough breeds with images for the game")
        }

        return selectedBreeds.map { breed ->
            val images = breedFetcher.allImagesForBreed(breed.id)
            Pair(breed, images.random().asViewBreedImage())
        }
    }

    // Quiz Category II
    suspend fun guessTheFactFetch(): List<QuizQuestion> = runBlocking {
        val allBreeds = breedFetcher.allBreeds().takeIf { it.isNotEmpty() } ?: breedFetcher.fetchAllBreeds().run { breedFetcher.allBreeds() }

        listOf(
            generateGuessTheBreedNameQuestion(allBreeds),
            generateOutlierTemperamentQuestion(allBreeds),
            generateCorrectTemperamentQuestion(allBreeds)
        )
    }

    // [Quiz Category II] Guess the Breed Name
    private suspend fun generateGuessTheBreedNameQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        val breedQuestionBreed = getRandomBreedWithImages(allBreeds)
        val breedQuestionImage = getRandomImageForBreed(breedQuestionBreed.id)
        val breedQuestionOptions = generateShuffledOptions(breedQuestionBreed.name, allBreeds.map { it.name })

        return QuizQuestion(
            QuestionType.GUESS_THE_BREED,
            Pair(breedQuestionBreed, breedQuestionImage),
            breedQuestionOptions,
            breedQuestionOptions.indexOf(breedQuestionBreed.name)
        )
    }

    // [Quiz Category II] Guess the Outlier Temperament
    private suspend fun generateOutlierTemperamentQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        val outlierQuestionBreed = getRandomBreedWithImages(allBreeds)
        val outlierQuestionImage = getRandomImageForBreed(outlierQuestionBreed.id)
        val outlierTemperaments = outlierQuestionBreed.temperament.shuffled().take(3)
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filter { it !in outlierTemperaments }

        val outlierQuestionOptions = (outlierTemperaments + otherTemperaments.random()).shuffled()
        val outlierQuestionCorrectAnswer = outlierQuestionOptions.indexOfFirst { it !in outlierTemperaments }

        return QuizQuestion(
            QuestionType.GUESS_THE_OUTLIER_TEMPERAMENT,
            Pair(outlierQuestionBreed, outlierQuestionImage),
            outlierQuestionOptions,
            outlierQuestionCorrectAnswer
        )
    }

    // [Quiz Category II] Guess the Correct Temperament
    private suspend fun generateCorrectTemperamentQuestion(allBreeds: List<UIBreed>): QuizQuestion {
        val temperamentQuestionBreed = getRandomBreedWithImages(allBreeds)
        val temperamentQuestionImage = getRandomImageForBreed(temperamentQuestionBreed.id)
        val correctTemperament = temperamentQuestionBreed.temperament.random()
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filter { it != correctTemperament }

        val temperamentQuestionOptions = (otherTemperaments.shuffled().take(3) + correctTemperament).shuffled()
        val temperamentQuestionCorrectAnswer = temperamentQuestionOptions.indexOf(correctTemperament)

        return QuizQuestion(
            QuestionType.GUESS_THE_CORRECT_TEMPERAMENT,
            Pair(temperamentQuestionBreed, temperamentQuestionImage),
            temperamentQuestionOptions,
            temperamentQuestionCorrectAnswer
        )
    }

    // Helper
    private suspend fun getRandomBreedWithImages(allBreeds: List<UIBreed>): UIBreed {
        val breed = allBreeds.random()
        if (breedFetcher.allImagesForBreed(breed.id).isEmpty()) {
            breedFetcher.fetchBreedImagesSafe(breed.id)
        }
        return breed
    }

    private fun getRandomImageForBreed(breedId: String): UIBreedImage {
        val images = breedFetcher.allImagesForBreed(breedId)
        if (images.isEmpty()) {
            throw Exception("No images available for breed $breedId")
        }
        return images.random().asViewBreedImage()
    }

    private fun generateShuffledOptions(correctOption: String, allOptions: List<String>): List<String> {
        return (allOptions.filter { it != correctOption }.shuffled().take(3) + correctOption).shuffled()
    }
}
