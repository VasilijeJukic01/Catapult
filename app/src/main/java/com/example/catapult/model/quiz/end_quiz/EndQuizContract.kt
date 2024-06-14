package com.example.catapult.model.quiz.end_quiz

interface EndQuizContract {

    sealed class EndQuizUIEvent{
        data class Submit(val submitted: Int, val category: Int, val result: Float) : EndQuizUIEvent()
    }

}