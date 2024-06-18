package com.example.catapult.repository.fetchers

import com.example.catapult.debug.ErrorTracker
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.mappers.asViewBreedImage
import com.example.catapult.model.quiz.GuessCatQuestion
import com.example.catapult.model.quiz.GuessCatQuestionType
import com.example.catapult.model.quiz.GuessFactQuestionType
import com.example.catapult.model.quiz.GuessFactQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestionType

class QuizGenerator(
    private val breedFetcher: BreedFetcher,
    private val errorTracker: ErrorTracker
) {

    // Quiz I
    suspend fun guessTheCatFetch(): List<GuessCatQuestion> {
        return try {
            val allBreeds = fetchAllBreedsIfNeeded()
            val allTemperaments = allBreeds.flatMap { it.temperament }.distinct()

            listOf(
                generateTemperamentQuestion(allBreeds, allTemperaments),
                generateBreedQuestion(allBreeds)
            )
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator I", "Failed to fetch quiz questions: ${e.message}")
            emptyList()
        }
    }

    // Quiz II
    suspend fun guessTheFactFetch(): List<GuessFactQuestion> {
        return try {
            val allBreeds = fetchAllBreedsIfNeeded()

            return listOf(
                generateGuessTheBreedNameQuestion(allBreeds),
                generateOutlierTemperamentQuestion(allBreeds),
                generateCorrectTemperamentQuestion(allBreeds)
            )
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator II", "Failed to fetch quiz questions: ${e.message}")
            emptyList()
        }
    }

    // Quiz III
    suspend fun leftOrRightFetch(): List<LeftOrRightQuestion> {
        return try {
            val allBreeds = fetchAllBreedsIfNeeded()

            return listOf(
                generateWhichCatIsHeavierQuestion(allBreeds),
                generateWhichCatLivesLongerQuestion(allBreeds)
            )
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator III", "Failed to fetch quiz questions: ${e.message}")
            emptyList()
        }
    }

    // Quiz I [Guess The Breed]
    private fun generateBreedQuestion(allBreeds: List<UIBreed>): GuessCatQuestion {
        val chosenBreed = allBreeds.random()
        val selectedBreeds = (listOf(chosenBreed) + allBreeds.filter { it != chosenBreed }.shuffled().take(3)).shuffled()
        val breedAndImages = selectedBreeds.map { it to getRandomImageForBreed(it.id) }

        return GuessCatQuestion(
            GuessCatQuestionType.GUESS_THE_BREED,
            breedAndImages,
            "",
            breedAndImages.indexOfFirst { it.first == chosenBreed }
        )
    }

    // Quiz I [Guess The Temperament]
    private fun generateTemperamentQuestion(allBreeds: List<UIBreed>, allTemperaments: List<String>): GuessCatQuestion {
        val randomTemperament = allTemperaments.random()
        val matchingBreeds = allBreeds.filter { it.temperament.contains(randomTemperament) }
        val nonMatchingBreeds = allBreeds.filter { !it.temperament.contains(randomTemperament) }

        val selectedBreeds = (listOf(matchingBreeds.random()) + nonMatchingBreeds.shuffled().take(3))
            .filter { breedFetcher.allImagesForBreed(it.id).isNotEmpty() }

        val breedAndImages = selectedBreeds.map { it to getRandomImageForBreed(it.id) }
        val correctAnswer = breedAndImages.indexOfFirst { it.first.temperament.contains(randomTemperament) }

        return GuessCatQuestion(
            GuessCatQuestionType.GUESS_THE_TEMPERAMENT,
            breedAndImages,
            randomTemperament,
            correctAnswer
        )
    }

    // Quiz II [Guess The Breed]
    private suspend fun generateGuessTheBreedNameQuestion(allBreeds: List<UIBreed>): GuessFactQuestion {
        val breed = getRandomBreedWithImages(allBreeds)
        val options = generateShuffledOptions(breed.name, allBreeds.map { it.name })

        return GuessFactQuestion(
            GuessFactQuestionType.GUESS_THE_BREED,
            breed to getRandomImageForBreed(breed.id),
            options,
            options.indexOf(breed.name)
        )
    }

    // Quiz II [Guess The Outlier Temperament]
    private suspend fun generateOutlierTemperamentQuestion(allBreeds: List<UIBreed>): GuessFactQuestion {
        val breed = getRandomBreedWithImages(allBreeds)
        val outlierTemperaments = breed.temperament.shuffled().take(3)
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filterNot { it in outlierTemperaments }
        val options = (outlierTemperaments + otherTemperaments.random()).shuffled()

        return GuessFactQuestion(
            GuessFactQuestionType.GUESS_THE_OUTLIER_TEMPERAMENT,
            breed to getRandomImageForBreed(breed.id),
            options,
            options.indexOfFirst { it !in outlierTemperaments }
        )
    }

    // Quiz II [Guess The Correct Temperament]
    private suspend fun generateCorrectTemperamentQuestion(allBreeds: List<UIBreed>): GuessFactQuestion {
        val breed = getRandomBreedWithImages(allBreeds)
        val correctTemperament = breed.temperament.random()
        val otherTemperaments = allBreeds.flatMap { it.temperament }.distinct().filterNot { it == correctTemperament }
        val options = (otherTemperaments.shuffled().take(3) + correctTemperament).shuffled()

        return GuessFactQuestion(
            GuessFactQuestionType.GUESS_THE_CORRECT_TEMPERAMENT,
            breed to getRandomImageForBreed(breed.id),
            options,
            options.indexOf(correctTemperament)
        )
    }

    // Quiz III [Which Cat Is Heavier]
    private fun generateWhichCatIsHeavierQuestion(allBreeds: List<UIBreed>): LeftOrRightQuestion {
        val (breed1, breed2) = getRandomBreeds(allBreeds)

        return LeftOrRightQuestion(
            LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER,
            breed1 to getRandomImageForBreed(breed1.id),
            breed2 to getRandomImageForBreed(breed2.id),
            if (breed1.weight > breed2.weight) 0 else 1
        )
    }

    // Quiz III [Which Cat Lives Longer]
    private fun generateWhichCatLivesLongerQuestion(allBreeds: List<UIBreed>): LeftOrRightQuestion {
        val (breed1, breed2) = getRandomBreeds(allBreeds)

        return LeftOrRightQuestion(
            LeftOrRightQuestionType.WHICH_CAT_LIVES_LONGER,
            breed1 to getRandomImageForBreed(breed1.id),
            breed2 to getRandomImageForBreed(breed2.id),
            if (breed1.lifeSpan > breed2.lifeSpan) 0 else 1
        )
    }

    // Utils
    private suspend fun fetchAllBreedsIfNeeded(): List<UIBreed> {
        return try {
            breedFetcher.allBreeds()
                .takeIf { it.isNotEmpty() }
                ?: breedFetcher.fetchAllBreeds().run { breedFetcher.allBreeds() }
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator", "Failed to fetch all breeds: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getRandomBreedWithImages(allBreeds: List<UIBreed>): UIBreed {
        return try {
            val breed = allBreeds.random()
            if (breedFetcher.allImagesForBreed(breed.id).isEmpty()) breedFetcher.fetchBreedImagesSafe(breed.id)
            breed
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator", "Failed to fetch breed images: ${e.message}")
            UIBreed()
        }
    }

    private fun getRandomImageForBreed(breedId: String): UIBreedImage {
        return try {
            breedFetcher.allImagesForBreed(breedId).random().asViewBreedImage()
        } catch (e: Exception) {
            errorTracker.logError("QuizGenerator", "Failed to fetch breed image: ${e.message}")
            UIBreedImage()
        }
    }

    private fun generateShuffledOptions(correctOption: String, allOptions: List<String>): List<String> {
        return (allOptions.filterNot { it == correctOption }.shuffled().take(3) + correctOption).shuffled()
    }

    private fun getRandomBreeds(allBreeds: List<UIBreed>): Pair<UIBreed, UIBreed> {
        val breed1 = allBreeds.random()
        val breed2 = allBreeds.filterNot { it == breed1 }.random()
        return breed1 to breed2
    }
}
