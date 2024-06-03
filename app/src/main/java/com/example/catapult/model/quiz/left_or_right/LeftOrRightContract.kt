package com.example.catapult.model.quiz.left_or_right

import com.example.catapult.model.catalog.UIBreedImage

interface LeftOrRightContract {

    data class LeftOrRightState(
        val question: String = "",
        val catImages: Pair<UIBreedImage, UIBreedImage> = Pair(UIBreedImage(), UIBreedImage()),
        val correctAnswer: Int = -1,
        val totalCorrect: Int = 0,
        val currentQuestionNumber: Int = 0,
        val isCorrectAnswer: Boolean? = null
    )

    sealed class LeftOrRightUiEvent{
        data class SelectLeftOrRight(val index: Int) : LeftOrRightUiEvent()
        data class NextQuestion(val correct: Boolean) : LeftOrRightUiEvent()
    }

}