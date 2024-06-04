package com.example.catapult.model.quiz.guess_cat

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.quiz.GuessFactQuestionType
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.catapult.model.quiz.guess_cat.GuessCatContract.GuessTheCatState
import com.example.catapult.model.quiz.guess_cat.GuessCatContract.GuessTheCatUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuessCatViewModel (
    private val repository: BreedRepository = BreedRepository
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
                fetchGuessTheCatQuestions()
                setState { copy(isCorrectAnswer = null) }
            }
            // Time Up
            is GuessTheCatUiEvent.TimeUp -> {
                // TODO: Handle Time Up
            }
        }
    }

    // Fetch
    private fun fetchGuessTheCatQuestions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val catBreedsAndImages = repository.guessTheCatFetch()
                val catImages = catBreedsAndImages.map { it.second }
                val correctAnswer = catImages.indices.random()

                val questionType = GuessFactQuestionType.entries.toTypedArray().random()
                val question = when (questionType) {
                    GuessFactQuestionType.GUESS_THE_TEMPERAMENT -> "Which cat is ${catBreedsAndImages[correctAnswer].first.temperament.random()}?"
                    GuessFactQuestionType.GUESS_THE_BREED -> "Which cat is of the breed ${catBreedsAndImages[correctAnswer].first.name}?"
                }

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