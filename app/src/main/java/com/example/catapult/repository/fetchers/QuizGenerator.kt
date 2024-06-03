package com.example.catapult.repository.fetchers

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.mappers.asViewBreedImage
import com.example.catapult.model.quiz.GuessCatQuestionType
import com.example.catapult.model.quiz.GuessCatQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestionType
import kotlinx.coroutines.runBlocking

class QuizGenerator(private val breedFetcher: BreedFetcher) {

    // Quiz Category I
    fun guessTheCatFetch(): List<Pair<UIBreed, UIBreedImage>> {
        val allBreeds = breedFetcher.allBreeds()
        val allTemperaments = allBreeds.flatMap { it.temperament }.distinct()

        val questionType = listOf("1", "2").random()

        return when (questionType) {
            "1" -> generateTemperamentQuestion(allBreeds, allTemperaments)
            "2" -> generateBreedQuestion(allBreeds)
            else -> throw Exception("Invalid question type")
        }
    }

    // [Quiz Category I] Guess the Breed Temperament by Image
    private fun generateTemperamentQuestion(allBreeds: List<UIBreed>, allTemperaments: List<String>): List<Pair<UIBreed, UIBreedImage>> {
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

    // [Quiz Category I] Guess the Breed Name by Image
    private fun generateBreedQuestion(allBreeds: List<UIBreed>): List<Pair<UIBreed, UIBreedImage>> {
        val chosenBreed = allBreeds.random()
        val otherBreeds = allBreeds.filter { it != chosenBreed }.shuffled().take(3)
        val selectedBreeds = (listOf(chosenBreed) + otherBreeds).shuffled()

        return selectedBreeds.map { breed ->
            val images = breedFetcher.allImagesForBreed(breed.id)
            Pair(breed, images.random().asViewBreedImage())
        }
    }

    // Quiz Category II
    suspend fun guessTheFactFetch(): List<GuessCatQuestion> = runBlocking {
        val allBreeds = breedFetcher.allBreeds().takeIf { it.isNotEmpty() } ?: breedFetcher.fetchAllBreeds().run { breedFetcher.allBreeds() }

        listOf(
            generateGuessTheBreedNameQuestion(allBreeds),
            generateOutlierTemperamentQuestion(allBreeds),
            generateCorrectTemperamentQuestion(allBreeds)
        )
    }

    // [Quiz Category II] Guess the Breed Name
    private suspend fun generateGuessTheBreedNameQuestion(allBreeds: List<UIBreed>): GuessCatQuestion {
        val breedQuestionBreed = getRandomBreedWithImages(allBreeds)
        val breedQuestionImage = getRandomImageForBreed(breedQuestionBreed.id)
        val breedQuestionOptions = generateShuffledOptions(breedQuestionBreed.name, allBreeds.map { it.name })

        return GuessCatQuestion(
            GuessCatQuestionType.GUESS_THE_BREED,
            Pair(breedQuestionBreed, breedQuestionImage),
            breedQuestionOptions,
            breedQuestionOptions.indexOf(breedQuestionBreed.name)
        )
    }

    // [Quiz Category II] Guess the Outlier Temperament
    private suspend fun generateOutlierTemperamentQuestion(allBreeds: List<UIBreed>): GuessCatQuestion {
        val outlierQuestionBreed = getRandomBreedWithImages(allBreeds)
        val outlierQuestionImage = getRandomImageForBreed(outlierQuestionBreed.id)
        val outlierTemperaments = outlierQuestionBreed.temperament.shuffled().take(3)
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filter { it !in outlierTemperaments }

        val outlierQuestionOptions = (outlierTemperaments + otherTemperaments.random()).shuffled()
        val outlierQuestionCorrectAnswer = outlierQuestionOptions.indexOfFirst { it !in outlierTemperaments }

        return GuessCatQuestion(
            GuessCatQuestionType.GUESS_THE_OUTLIER_TEMPERAMENT,
            Pair(outlierQuestionBreed, outlierQuestionImage),
            outlierQuestionOptions,
            outlierQuestionCorrectAnswer
        )
    }

    // [Quiz Category II] Guess the Correct Temperament
    private suspend fun generateCorrectTemperamentQuestion(allBreeds: List<UIBreed>): GuessCatQuestion {
        val temperamentQuestionBreed = getRandomBreedWithImages(allBreeds)
        val temperamentQuestionImage = getRandomImageForBreed(temperamentQuestionBreed.id)
        val correctTemperament = temperamentQuestionBreed.temperament.random()
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filter { it != correctTemperament }

        val temperamentQuestionOptions = (otherTemperaments.shuffled().take(3) + correctTemperament).shuffled()
        val temperamentQuestionCorrectAnswer = temperamentQuestionOptions.indexOf(correctTemperament)

        return GuessCatQuestion(
            GuessCatQuestionType.GUESS_THE_CORRECT_TEMPERAMENT,
            Pair(temperamentQuestionBreed, temperamentQuestionImage),
            temperamentQuestionOptions,
            temperamentQuestionCorrectAnswer
        )
    }

    //QUIZ CATEGORY III
    suspend fun leftOrRightFetch(): List<LeftOrRightQuestion> {
        val allBreeds = breedFetcher.allBreeds().takeIf { it.isNotEmpty() } ?: breedFetcher.fetchAllBreeds().run { breedFetcher.allBreeds() }

        return listOf(
            generateWhichCatIsHeavierQuestion(allBreeds),
            generateWhichCatLivesLongerQuestion(allBreeds)
        )
    }

    // [Quiz Category III] Which Cat is Heavier?
    private fun generateWhichCatIsHeavierQuestion(allBreeds: List<UIBreed>): LeftOrRightQuestion {
        val breed1 = allBreeds.random()
        val breed2 = allBreeds.filter { it != breed1 }.random()

        val breed1Image = getRandomImageForBreed(breed1.id)
        val breed2Image = getRandomImageForBreed(breed2.id)

        return LeftOrRightQuestion(
            LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER,
            Pair(breed1, breed1Image),
            Pair(breed2, breed2Image),
            if (breed1.weight > breed2.weight) 0 else 1
        )
    }

    // [Quiz Category III] Which Cat Lives Longer?
    private fun generateWhichCatLivesLongerQuestion(allBreeds: List<UIBreed>): LeftOrRightQuestion {
        val breed1 = allBreeds.random()
        val breed2 = allBreeds.filter { it != breed1 }.random()

        val breed1Image = getRandomImageForBreed(breed1.id)
        val breed2Image = getRandomImageForBreed(breed2.id)

        return LeftOrRightQuestion(
            LeftOrRightQuestionType.WHICH_CAT_LIVES_LONGER,
            Pair(breed1, breed1Image),
            Pair(breed2, breed2Image),
            if (breed1.lifeSpan > breed2.lifeSpan) 0 else 1
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
