package com.example.catapult.model.quiz.left_or_right

import android.os.CountDownTimer
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

@OptIn(FlowPreview::class)
class LeftOrRightViewModel(
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {

    private val stateFlow = MutableStateFlow(LeftOrRightState())
    val state = stateFlow.asStateFlow()

    private val eventsFlow = MutableSharedFlow<LeftOrRightUiEvent>()

    private fun setState(reducer: LeftOrRightState.() -> LeftOrRightState) =
        stateFlow.getAndUpdate(reducer)

    private val _navigateToEndQuiz = MutableStateFlow<Float?>(null)
    val navigateToEndQuiz = _navigateToEndQuiz.asStateFlow()

    private val timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }

        override fun onFinish() {
            setEvent(LeftOrRightUiEvent.TimeUp)
        }
    }

    init {
        viewModelScope.launch {
            eventsFlow
                .debounce(300)
                .collect { event ->
                handleEvent(event)
            }
        }
        fetchLeftOrRightQuestions()
        timer.start()
    }

    fun setEvent(event: LeftOrRightUiEvent) = viewModelScope.launch {
        eventsFlow.emit(event)
    }

    private fun handleEvent(event: LeftOrRightUiEvent) {
        when (event) {
            is LeftOrRightUiEvent.SelectLeftOrRight -> {
                val isCorrect = event.index == state.value.correctAnswer
                setState {
                    copy(
                        totalCorrect = if (isCorrect) totalCorrect + 1 else totalCorrect,
                        isCorrectAnswer = isCorrect
                    )
                }
                setEvent(LeftOrRightUiEvent.NextQuestion(isCorrect))
            }
            is LeftOrRightUiEvent.NextQuestion -> {
                if (state.value.currentQuestionNumber >= 20)
                    endQuiz()
                else {
                    fetchLeftOrRightQuestions()
                    setState { copy(isCorrectAnswer = null) }
                }
            }
            is LeftOrRightUiEvent.TimeUp -> endQuiz()
            is LeftOrRightUiEvent.EndQuiz -> Unit
        }
    }

    private fun endQuiz() {
        val stateValue = state.value
        val totalPoints = stateValue.totalCorrect * 2.5 * (1 + (stateValue.timeLeft + 120) / 300.0)
        _navigateToEndQuiz.value = totalPoints.toFloat().coerceAtMost(100.00f)
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
                    LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER -> "Which cat is heavier on average?"
                    LeftOrRightQuestionType.WHICH_CAT_LIVES_LONGER -> "Which cat lives longer on average?"
                }

                setState {
                    copy(
                        question = question,
                        catImages = Pair(firstBreedAndImage.second, secondBreedAndImage.second),
                        correctAnswer = correctAnswer,
                        currentQuestionNumber = currentQuestionNumber + 1
                    )
                }
            }
        }
    }
}

