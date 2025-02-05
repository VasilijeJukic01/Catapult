package com.example.catapult.model.quiz.guess_cat

import com.example.catapult.model.catalog.UIBreedImage

interface GuessCatContract {

    data class GuessTheCatState(
        val question: String = "",
        val catImages: List<UIBreedImage> = emptyList(),
        val correctAnswer: Int = -1,
        val totalCorrect: Int = 0,
        val currentQuestionNumber: Int = 0,
        val isCorrectAnswer: Boolean? = null,
        val timeLeft: Long = 0L,
        val quizEnded: Boolean = false,
        val totalPoints: Float = 0f,
        val usedImages: MutableSet<UIBreedImage> = mutableSetOf()
    )

    sealed class GuessTheCatUiEvent{
        data class SelectCatImage(val index: Int) : GuessTheCatUiEvent()
        data class NextQuestion(val correct: Boolean) : GuessTheCatUiEvent()
        data object TimeUp : GuessTheCatUiEvent()
        data class EndQuiz(val totalPoints: Float) : GuessTheCatUiEvent()
    }

}