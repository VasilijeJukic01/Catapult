package com.example.catapult.model.quiz.guess_fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class GuessFactViewModel (
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(GuessFactContract.GuessTheFactState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: GuessFactContract.GuessTheFactState.() -> GuessFactContract.GuessTheFactState) =
        stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<GuessFactContract.GuessTheFactUiEvent>()

    fun setEvent(event: GuessFactContract.GuessTheFactUiEvent) =
        viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchGuessTheFactQuestions()
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

    private fun handleEvent(event: GuessFactContract.GuessTheFactUiEvent) {
        when (event) {
            // Select Fact
            is GuessFactContract.GuessTheFactUiEvent.SelectFact -> {
                val isCorrect = event.index == state.value.correctAnswer
                setState { copy(totalCorrect = if (isCorrect) totalCorrect + 1 else totalCorrect, isCorrectAnswer = isCorrect) }
                setEvent(GuessFactContract.GuessTheFactUiEvent.NextQuestion(isCorrect))
            }
            // Next Question
            is GuessFactContract.GuessTheFactUiEvent.NextQuestion -> {
                fetchGuessTheFactQuestions()
                setState { copy(isCorrectAnswer = null) }
            }
        }
    }

    private fun fetchGuessTheFactQuestions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val questions = repository.guessTheFactFetch()
                val currentQuestion = questions.random()
                val questionType = when (currentQuestion.questionType) {
                    QuestionType.GUESS_THE_BREED -> "Guess the breed"
                    QuestionType.GUESS_THE_OUTLIER_TEMPERAMENT -> "Guess the outlier temperament"
                    QuestionType.GUESS_THE_CORRECT_TEMPERAMENT -> "Guess the correct temperament"
                }
                val catImage = currentQuestion.breedAndImage.second
                val correctAnswer = currentQuestion.correctAnswer

                setState {
                    copy(
                        question = questionType,
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