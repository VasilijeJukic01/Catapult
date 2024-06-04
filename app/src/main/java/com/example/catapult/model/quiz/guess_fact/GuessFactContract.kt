package com.example.catapult.model.quiz.guess_fact

import com.example.catapult.model.catalog.UIBreedImage

interface GuessFactContract {

    data class GuessTheFactState(
        val question: String = "",
        val options: List<String> = emptyList(),
        val catImage: UIBreedImage = UIBreedImage(),
        val correctAnswer: Int = -1,
        val totalCorrect: Int = 0,
        val currentQuestionNumber: Int = 0,
        val isCorrectAnswer: Boolean? = null,
        val timeLeft: Long = 0L
    )

    sealed class GuessTheFactUiEvent{
        data class SelectFact(val index: Int) : GuessTheFactUiEvent()
        data class NextQuestion(val correct: Boolean) : GuessTheFactUiEvent()
        data object TimeUp : GuessTheFactUiEvent()
        data class EndQuiz(val totalPoints: Float) : GuessTheFactUiEvent()
    }

}