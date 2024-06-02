package com.example.catapult.model.quiz.guess_cat

import com.example.catapult.model.catalog.UIBreedImage

interface GuessCatContract {

    data class GuessTheCatState(
        val question: String = "",
        val catImages: List<UIBreedImage> = emptyList(),
        val correctAnswer: Int = -1,
        val totalCorrect: Int = 0,
        val currentQuestionNumber: Int = 0,
        val isCorrectAnswer: Boolean? = null
    )

    sealed class GuessTheCatUiEvent{
        data class SelectCatImage(val index: Int) : GuessTheCatUiEvent()
        data class NextQuestion(val correct: Boolean) : GuessTheCatUiEvent()
    }

    enum class QuestionType {
        GUESS_THE_TEMPERAMENT,
        GUESS_THE_BREED
    }

    data class QuizQuestion(
        val questionType: QuestionType,
        val breedAndImages: List<Pair<String, UIBreedImage>>,
        val correctAnswer: Int
    )

}