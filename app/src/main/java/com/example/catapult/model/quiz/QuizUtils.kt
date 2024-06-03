package com.example.catapult.model.quiz

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage

enum class GuessFactQuestionType {
    GUESS_THE_TEMPERAMENT,
    GUESS_THE_BREED
}

data class GuessFactQuestion(
    val questionType: GuessFactQuestionType,
    val breedAndImages: List<Pair<String, UIBreedImage>>,
    val correctAnswer: Int
)

enum class GuessCatQuestionType {
    GUESS_THE_BREED,
    GUESS_THE_OUTLIER_TEMPERAMENT,
    GUESS_THE_CORRECT_TEMPERAMENT
}

data class GuessCatQuestion(
    val secondQuestionType: GuessCatQuestionType,
    val breedAndImage: Pair<UIBreed, UIBreedImage>,
    val options: List<String>,
    val correctAnswer: Int
)

enum class LeftOrRightQuestionType {
    WHICH_CAT_IS_HEAVIER,
    WHICH_CAT_LIVES_LONGER
}

data class LeftOrRightQuestion(
    val questionType: LeftOrRightQuestionType,
    val firstBreedAndImage: Pair<UIBreed, UIBreedImage>,
    val secondBreedAndImage: Pair<UIBreed, UIBreedImage>,
    val correctAnswer: Int
)
