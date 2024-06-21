package com.example.catapult

import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catapult.audio.AudioManager
import com.example.catapult.coroutines.DispatcherProvider
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.model.catalog.UIBreedImage
import com.example.catapult.model.quiz.LeftOrRightQuestion
import com.example.catapult.model.quiz.LeftOrRightQuestionType
import com.example.catapult.model.quiz.left_or_right.LeftOrRightContract.LeftOrRightUiEvent
import com.example.catapult.model.quiz.left_or_right.LeftOrRightViewModel
import com.example.catapult.repository.BreedRepository
import com.example.catapult.rules.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LeftOrRightViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var repository: BreedRepository
    private lateinit var audioManager: AudioManager
    private lateinit var viewModel: LeftOrRightViewModel

    // Initialize
    @Before
    fun setup() {
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        dispatcherProvider = coroutineTestRule.dispatcherProvider
        repository = mockk(relaxed = true)
        audioManager = mockk(relaxed = true)
        viewModel = LeftOrRightViewModel(dispatcherProvider, repository, audioManager)
    }

    @Test
    fun testInitialState() = runTest {
        // Arrange
        val leftOrRightQuestion = mockk<LeftOrRightQuestion> {
            every { firstBreedAndImage } returns Pair(UIBreed(), UIBreedImage())
            every { secondBreedAndImage } returns Pair(UIBreed(), UIBreedImage())
            every { correctAnswer } returns 0
            every { questionType } returns LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER
        }
        coEvery { repository.leftOrRightFetch() } returns listOf(leftOrRightQuestion)

        // Act
        viewModel.setEvent(LeftOrRightUiEvent.NextQuestion(true))
        delay(100)

        // Assert
        val state = viewModel.state.value
        assertEquals("Which cat is heavier on average?", state.question)
        assertNotNull(state.catImages)
        assertEquals(0, state.correctAnswer)
    }

    @Test
    fun testFetchLeftOrRightQuestions() = runTest {
        // Arrange
        val leftOrRightQuestion = mockk<LeftOrRightQuestion> {
            every { firstBreedAndImage } returns Pair(mockk(), mockk())
            every { secondBreedAndImage } returns Pair(mockk(), mockk())
            every { correctAnswer } returns 0
            every { questionType } returns LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER
        }
        coEvery { repository.leftOrRightFetch() } returns listOf(leftOrRightQuestion)

        // Act
        viewModel.setEvent(LeftOrRightUiEvent.NextQuestion(true))
        delay(100)

        // Assert
        val state = viewModel.state.value
        assertEquals("Which cat is heavier on average?", state.question)
        assertEquals(0, state.correctAnswer)
        assertEquals(1, state.currentQuestionNumber)
    }

    @Test
    fun testSelectLeftOrRightCorrect() = runTest {
        // Arrange
        val leftOrRightQuestion = mockk<LeftOrRightQuestion> {
            every { firstBreedAndImage } returns Pair(mockk(), UIBreedImage())
            every { secondBreedAndImage } returns Pair(mockk(), UIBreedImage())
            every { correctAnswer } returns 0
            every { questionType } returns LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER
        }
        coEvery { repository.leftOrRightFetch() } returns listOf(leftOrRightQuestion)

        // Act
        viewModel.setEvent(LeftOrRightUiEvent.SelectLeftOrRight(0))
        delay(400)

        // Assert
        val state = viewModel.state.value
        assertEquals(1, state.totalCorrect)
        assertTrue(state.isCorrectAnswer ?: false)
    }

    @Test
    fun testSelectLeftOrRightIncorrect() = runTest {
        // Arrange
        val leftOrRightQuestion = mockk<LeftOrRightQuestion> {
            every { firstBreedAndImage } returns Pair(mockk(), UIBreedImage())
            every { secondBreedAndImage } returns Pair(mockk(), UIBreedImage())
            every { correctAnswer } returns 0
            every { questionType } returns LeftOrRightQuestionType.WHICH_CAT_IS_HEAVIER
        }
        coEvery { repository.leftOrRightFetch() } returns listOf(leftOrRightQuestion)

        // Act
        viewModel.setEvent(LeftOrRightUiEvent.SelectLeftOrRight(1))
        delay(400)

        // Assert
        val state = viewModel.state.value
        assertEquals(0, state.totalCorrect)
        assertTrue(state.isCorrectAnswer == false)
    }

}

