package com.example.catapult.model.quiz.left_or_right

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.model.quiz.LeftOrRightQuestionType
import com.example.catapult.repository.BreedRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract.LeftOrRightState
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract.LeftOrRightUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext

class LeftOrRightViewModel (
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {

    // State
    private val stateFlow = MutableStateFlow(LeftOrRightState())
    val state = stateFlow.asStateFlow()

    private fun setState(reducer: LeftOrRightState.() -> LeftOrRightState) =
        stateFlow.getAndUpdate(reducer)

    // Event
    private val eventsFlow = MutableSharedFlow<LeftOrRightUiEvent>()

    fun setEvent(event: LeftOrRightUiEvent) =
        viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
        fetchLeftOrRightQuestions()
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

    private fun handleEvent(event: LeftOrRightUiEvent) {
        when (event) {
            // Select Left or Right
            is LeftOrRightUiEvent.SelectLeftOrRight -> {
                val isCorrect = event.index == state.value.correctAnswer
                setState { copy(totalCorrect = if (isCorrect) totalCorrect + 1 else totalCorrect, isCorrectAnswer = isCorrect) }
                setEvent(LeftOrRightUiEvent.NextQuestion(isCorrect))
            }
            // Next Question
            is LeftOrRightUiEvent.NextQuestion -> {
                fetchLeftOrRightQuestions()
                setState { copy(isCorrectAnswer = null) }
            }
        }
    }

    private fun fetchLeftOrRightQuestions() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val leftOrRightQuestions = repository.leftOrRightFetch()
                val currentQuestion = leftOrRightQuestions.random()

                val firstBreedAndImage = currentQuestion.firstBreedAndImage
                val secondBreedAndImage = currentQuestion.secondBreedAndImage
                val correctAnswer = currentQuestion.correctAnswer

                val questionType = currentQuestion.questionType
                val question = when (questionType) {
                    LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER -> "Which cat is heavier?"
                    LeftOrRightQuestionType.WHICH_CAT_LIVES_LONGER -> "Which cat lives longer?"
                }

                setState { copy(
                    question = question,
                    catImages = Pair(firstBreedAndImage.second, secondBreedAndImage.second),
                    correctAnswer = correctAnswer,
                    currentQuestionNumber = currentQuestionNumber + 1)
                }
            }
        }
    }


}
