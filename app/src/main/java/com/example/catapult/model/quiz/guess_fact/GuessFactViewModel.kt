package com.example.catapult.model.quiz.guess_fact

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.quiz.GuessFactQuestionType
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.catapult.model.quiz.guess_fact.GuessFactContract.GuessTheFactState
import com.example.catapult.model.quiz.guess_fact.GuessFactContract.GuessTheFactUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuessFactViewModel @Inject constructor (
    private val repository: BreedRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(GuessTheFactState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: GuessTheFactState.() -> GuessTheFactState) =
        stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<GuessTheFactUiEvent>()

    fun setEvent(event: GuessTheFactUiEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    // Timer
    private val timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }
        override fun onFinish() {
            setEvent(GuessTheFactUiEvent.TimeUp)
        }
    }

    init {
        handleEvents()
        fetchGuessTheFactQuestions()
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

    private fun handleEvent(event: GuessTheFactUiEvent) {
        when (event) {
            // Select Fact
            is GuessTheFactUiEvent.SelectFact -> {
                val isCorrect = event.index == state.value.correctAnswer
                setState { copy(totalCorrect = if (isCorrect) totalCorrect + 1 else totalCorrect, isCorrectAnswer = isCorrect) }
                setEvent(GuessTheFactUiEvent.NextQuestion(isCorrect))
            }
            // Next Question
            is GuessTheFactUiEvent.NextQuestion -> {
                if (state.value.currentQuestionNumber >= 20)
                    endQuiz()
                else {
                    fetchGuessTheFactQuestions()
                    setState { copy(isCorrectAnswer = null) }
                }
            }
            // Time Up
            is GuessTheFactUiEvent.TimeUp -> {
                endQuiz()
            }
            // End Quiz
            is GuessTheFactUiEvent.EndQuiz -> Unit
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

    private fun fetchGuessTheFactQuestions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val questions = repository.guessTheFactFetch()
                val currentQuestion = questions.random()

                val questionType = currentQuestion.questionType
                val question = when (questionType) {
                    GuessFactQuestionType.GUESS_THE_BREED -> "Which cat is this?"
                    GuessFactQuestionType.GUESS_THE_OUTLIER_TEMPERAMENT -> "Which temperament is not associated with the breed?"
                    GuessFactQuestionType.GUESS_THE_CORRECT_TEMPERAMENT -> "Which temperament is associated with this breed?"
                }

                val catImage = currentQuestion.breedAndImage.second
                val correctAnswer = currentQuestion.correctAnswer

                setState {
                    copy(
                        question = question,
                        options = currentQuestion.options,
                        catImage = catImage,
                        correctAnswer = correctAnswer,
                        currentQuestionNumber = currentQuestionNumber + 1
                    )
                }
            }
        }
    }

}