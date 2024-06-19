package com.example.catapult

import android.os.Looper
import android.util.Log
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.catapult.coroutines.DispatcherProvider
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.quiz.LeftOrRightQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestionType
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract
import com.example.catapult.model.quiz.left_or_right.LeftOrRightViewModel
import com.example.catapult.repository.BreedRepository
import com.example.catapult.ui.compose.quiz.LeftOrRightScreen
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeftOrRightCatScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun skip_question_test() {
        // Arrange
        val questionNumber = mutableStateOf(4)
        val totalPoints = mutableStateOf(0)
        composeTestRule.setContent {
            CompositionLocalProvider {
                LeftOrRightScreen(
                    state = LeftOrRightContract.LeftOrRightState(currentQuestionNumber = questionNumber.value),
                    onCatImageClick = {},
                    onSkipClick = { questionNumber.value++ },
                    onBackClick = {},
                )
            }
        }

        // Act
        composeTestRule.onNodeWithText("Skip Question").performClick()

        // Assert
        composeTestRule
            .onNodeWithText("Question 5 of 20")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Total Points: 0")
            .assertIsDisplayed()
    }

    @Before
    fun setUp() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
    }


    @Test
    fun correct_answer_increases_total_correct_and_question_number() {
        val correctAnswerIndex = 0

        val mockQuestions = listOf(
            LeftOrRightQuestion(
                questionType = LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER,
                firstBreedAndImage = Pair(
                    UIBreed(
                        "Breed1",
                        "id1",
                    ), UIBreedImage("url1", "id1")
                ),
                secondBreedAndImage = Pair(
                    UIBreed(
                        "Breed2",
                        "id2",
                    ), UIBreedImage("url2", "id2")
                ),
                correctAnswer = correctAnswerIndex
            )
        )
        val repository: BreedRepository = mockk()
        coEvery { repository.leftOrRightFetch() } returns mockQuestions

        val viewModel = LeftOrRightViewModel(repository = repository, dispatcherProvider = DispatcherProvider())

        val initialState = mutableStateOf(viewModel.state.value.copy(totalCorrect = 0, currentQuestionNumber = 1))

        composeTestRule.setContent {
            CompositionLocalProvider {
                LeftOrRightScreen(
                    state = initialState.value,
                    onCatImageClick = { index ->
                        viewModel.setEvent(
                            LeftOrRightContract.LeftOrRightUiEvent.SelectLeftOrRight(
                                index
                            )
                        )
                        initialState.value = viewModel.state.value
                    },
                    onSkipClick = {},
                    onBackClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(
            "LeftOrRightCatScreen::CatImage $correctAnswerIndex",
            useUnmergedTree = true
        ).performClick()

        composeTestRule.waitUntil(timeoutMillis = 1000) { initialState.value.totalCorrect == 1 }

        composeTestRule
            .onNodeWithTag("LeftOrRightCatScreen::TopAppBar::TotalCorrect")
            .assertTextEquals("Total Points: 1")
    }
}