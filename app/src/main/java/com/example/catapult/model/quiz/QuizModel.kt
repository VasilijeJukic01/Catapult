package com.example.catapult.model.quiz

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage

// Quiz I
enum class GuessCatQuestionType {
    GUESS_THE_TEMPERAMENT,
    GUESS_THE_BREED
}

data class GuessCatQuestion(
    val questionType: GuessCatQuestionType,
    val breedAndImages: List<Pair<UIBreed, UIBreedImage>> = emptyList(),
    val temperament: String = "",
    val correctAnswer: Int = -1
)

// Quiz II
enum class GuessFactQuestionType {
    GUESS_THE_BREED,
    GUESS_THE_OUTLIER_TEMPERAMENT,
    GUESS_THE_CORRECT_TEMPERAMENT
}

data class GuessFactQuestion(
    val questionType: GuessFactQuestionType,
    val breedAndImage: Pair<UIBreed, UIBreedImage> = Pair(UIBreed(), UIBreedImage()),
    val options: List<String> = emptyList(),
    val correctAnswer: Int = -1
)

// Quiz III
enum class LeftOrRightQuestionType {
    WHICH_CAT_IS_HEAVIER,
    WHICH_CAT_LIVES_LONGER
}

data class LeftOrRightQuestion(
    val questionType: LeftOrRightQuestionType,
    val firstBreedAndImage: Pair<UIBreed, UIBreedImage> = Pair(UIBreed(), UIBreedImage()),
    val secondBreedAndImage: Pair<UIBreed, UIBreedImage> = Pair(UIBreed(), UIBreedImage()),
    val correctAnswer: Int = -1
)
