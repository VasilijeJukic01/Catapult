package com.example.catapult.model.quiz

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage

enum class FirstQuestionType {
    GUESS_THE_TEMPERAMENT,
    GUESS_THE_BREED
}

data class FirstQuizQuestion(
    val questionType: FirstQuestionType,
    val breedAndImages: List<Pair<String, UIBreedImage>>,
    val correctAnswer: Int
)

enum class SecondQuestionType {
    GUESS_THE_BREED,
    GUESS_THE_OUTLIER_TEMPERAMENT,
    GUESS_THE_CORRECT_TEMPERAMENT
}

data class SecondQuizQuestion(
    val secondQuestionType: SecondQuestionType,
    val breedAndImage: Pair<UIBreed, UIBreedImage>,
    val options: List<String>,
    val correctAnswer: Int
)