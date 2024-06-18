package com.example.catapult.model.quiz.guess_cat

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.quiz.GuessCatQuestionType
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.catapult.model.quiz.guess_cat.GuessCatContract.GuessTheCatState
import com.example.catapult.model.quiz.guess_cat.GuessCatContract.GuessTheCatUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuessCatViewModel @Inject constructor (
    private val repository: BreedRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(GuessTheCatState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: GuessTheCatState.() -> GuessTheCatState) = stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<GuessTheCatUiEvent>()

    fun setEvent(event: GuessTheCatUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    // Timer
    private val timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }
        override fun onFinish() {
            setEvent(GuessTheCatUiEvent.TimeUp)
        }
    }

    init {
        handleEvents()
        fetchGuessTheCatQuestions()
        timer.start()
    }

    // Events
    @OptIn(FlowPreview::class)
    private fun handleEvents() {
        viewModelScope.launch {
            eventsFlow
                .debounce(300)
                .collect { event ->
                    handleEvent(event)
                }
        }
    }

    private fun handleEvent(event: GuessTheCatUiEvent) {
        when (event) {
            // Select Cat Image
            is GuessTheCatUiEvent.SelectCatImage -> {
                val isCorrect = event.index == state.value.correctAnswer
                setState { copy(totalCorrect = if (isCorrect) totalCorrect + 1 else totalCorrect, isCorrectAnswer = isCorrect) }
                setEvent(GuessTheCatUiEvent.NextQuestion(isCorrect))
            }
            // Next Question
            is GuessTheCatUiEvent.NextQuestion -> {
                if (state.value.currentQuestionNumber >= 20)
                    endQuiz()
                else {
                    fetchGuessTheCatQuestions()
                    setState { copy(isCorrectAnswer = null) }
                }
            }
            // Time Up
            is GuessTheCatUiEvent.TimeUp -> endQuiz()
            // End Quiz
            is GuessTheCatUiEvent.EndQuiz -> Unit
        }
    }

    private fun endQuiz() {
        val stateValue = state.value
        val totalPoints = stateValue.totalCorrect * 2.5 * (1 + (stateValue.timeLeft + 120) / 300.0)
        setState {
            copy(
                quizEnded = true,
                totalPoints = totalPoints.toFloat().coerceAtMost(maximumValue = 100.00f)
            )
        }
    }

    // Fetch
    private fun fetchGuessTheCatQuestions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val questions = repository.guessTheCatFetch()
                val currentQuestion = questions.random()

                val questionType = currentQuestion.questionType

                val breed = currentQuestion.breedAndImages[currentQuestion.correctAnswer].first
                val question = when (questionType) {
                    GuessCatQuestionType.GUESS_THE_TEMPERAMENT -> "Which cat is ${currentQuestion.temperament}?"
                    GuessCatQuestionType.GUESS_THE_BREED -> "Which cat is of the breed ${breed.name}?"
                }

                val correctAnswer = currentQuestion.correctAnswer
                val catImages = currentQuestion.breedAndImages.map { it.second }

                setState { copy(
                    question = question,
                    catImages = catImages,
                    correctAnswer = correctAnswer,
                    currentQuestionNumber = currentQuestionNumber + 1)
                }
            }
        }
    }
}