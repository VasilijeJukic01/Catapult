package com.example.catapult

import com.example.catapult.coroutines.CoroutinesTestRule
import com.example.catapult.coroutines.DispatcherProvider
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract
import com.example.catapult.model.quiz.left_or_right.LeftOrRightViewModel
import com.example.catapult.repository.BreedRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LeftOrRightViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun handleNextQuestion_callsNextQuestion() = runTest {
        val expectedQuestionNumber = 5
        val repository = mockk<BreedRepository>(relaxed = true)
        val viewModel = LeftOrRightViewModel(
            dispatcherProvider = DispatcherProvider(),
            repository = repository
        )
        viewModel.setEvent(
            LeftOrRightContract.LeftOrRightUiEvent.NextQuestion(
                correct = true,
            )
        )
        advanceUntilIdle()

        coVerify {
            repository.leftOrRightFetch()
        }
    }

}