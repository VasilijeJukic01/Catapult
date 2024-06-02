package com.example.catapult.model.quiz.guess_fact

import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage

data class QuizQuestion(
    val questionType: QuestionType,
    val breedAndImage: Pair<UIBreed, UIBreedImage>,
    val options: List<String>,
    val correctAnswer: Int
)
