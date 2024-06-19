package com.example.catapult

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catapult.debug.ErrorTracker
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.repository.fetchers.BreedFetcher
import com.example.catapult.repository.fetchers.QuizGenerator
import com.example.catapult.rules.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.mockk
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
class LeftOrRightGeneratorTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private lateinit var breedFetcher: BreedFetcher
    private lateinit var errorTracker: ErrorTracker
    private lateinit var quizGenerator: QuizGenerator

    @Before
    fun setup() {
        breedFetcher = mockk(relaxed = true)
        errorTracker = mockk(relaxed = true)
        quizGenerator = QuizGenerator(breedFetcher, errorTracker)
    }

    @Test
    fun leftOrRightFetch_fetchFailed() = runTest {
        // Arrange
        coEvery { breedFetcher.allBreeds() } throws Exception()

        // Act
        val result = quizGenerator.leftOrRightFetch()
        delay(100)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun leftOrRightFetch_fetchEmpty() = runTest {
        // Arrange
        coEvery { breedFetcher.allBreeds() } returns emptyList()

        // Act
        val result = quizGenerator.leftOrRightFetch()
        delay(100)

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun leftOrRightFetch_fetchSuccessful() = runTest {
        // Arrange
        val mockBreeds = listOf(UIBreed("1"), UIBreed("2"))
        coEvery { breedFetcher.allBreeds() } returns mockBreeds

        // Act
        val result = quizGenerator.leftOrRightFetch()
        delay(50)

        // Assert
        assertTrue(result.isNotEmpty())
        assertTrue(result.size == 2)
        assertTrue(result[0].secondBreedAndImage.first.id == "1")
        assertTrue(result[1].secondBreedAndImage.first.id == "2")
    }

}