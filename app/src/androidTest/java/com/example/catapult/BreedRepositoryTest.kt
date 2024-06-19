package com.example.catapult

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catapult.api.BreedsApi
import com.example.catapult.api.models.BreedApiModel
import com.example.catapult.rules.CoroutinesTestRule
import com.example.catapult.database.AppDatabase
import com.example.catapult.database.entities.Breed
import com.example.catapult.database.entities.BreedImage
import com.example.catapult.debug.ErrorTracker
import com.example.catapult.repository.BreedRepository
import com.example.catapult.repository.fetchers.QuizGenerator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class BreedRepositoryTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var breedsApi: BreedsApi
    private lateinit var errorTracker: ErrorTracker
    private lateinit var breedRepository: BreedRepository
    private lateinit var quizGenerator: QuizGenerator

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    // Initialize
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        breedsApi = mockk(relaxed = true)
        errorTracker = mockk(relaxed = true)
        quizGenerator = mockk(relaxed = true)

        breedRepository = BreedRepository(breedsApi, appDatabase, errorTracker)
    }

    // Tear Down
    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun fetchAllBreeds_insertsBreedsIntoDatabase() = runTest {
        val breedList = listOf(
            BreedApiModel(id = "1", name = "Breed1"),
            BreedApiModel(id = "2", name = "Breed2")
        )

        coEvery { breedsApi.getAllBreeds() } returns breedList

        breedRepository.fetchAllBreeds()

        val breedsInDb = appDatabase.breedDao().getAll()

        assertEquals(breedList.size, breedsInDb.size)
    }

    @Test
    fun observeAllBreeds_returnsBreedList() = runTest {
        // Arrange
        val breedList = listOf(
            Breed(id = "1", name = "Breed1"),
            Breed(id = "2", name = "Breed2")
        )
        appDatabase.breedDao().insertAll(breedList)

        // Act
        val breeds = breedRepository.observeAllBreeds().first()

        // Assert
        assertEquals(breedList.size, breeds.size)
    }

    @Test
    fun fetchBreedDetails_returnsCorrectBreed() = runTest {
        // Arrange
        val breedDb = Breed(id = "1", name = "Breed1")
        appDatabase.breedDao().insert(breedDb)

        // Act
        val breed = breedRepository.fetchBreedDetails("1")

        // Assert
        assertNotNull(breed)
        assertEquals(breedDb.id, breed?.id)
    }

    @Test
    fun allBreeds_returnsAllBreedsWithImages() = runTest {
        // Arrange
        val breedList = listOf(
            Breed(id = "1", name = "Breed1"),
            Breed(id = "2", name = "Breed2")
        )
        val breedImageList = listOf(
            BreedImage(id = "1", breedId = "1", url = "url1"),
            BreedImage(id = "2", breedId = "2", url = "url2")
        )
        appDatabase.breedDao().insertAll(breedList)
        appDatabase.breedImageDao().insertAll(breedImageList)

        // Act
        val breeds = breedRepository.allBreeds()

        // Assert
        assertEquals(2, breeds.size)
    }

    @Test
    fun allImagesForBreed_returnsImagesForGivenBreed() = runTest {
        // Arrange
        val breedList = listOf(
            Breed(id = "1", name = "Breed1"),
            Breed(id = "2", name = "Breed2")
        )
        val breedImages = listOf(
            BreedImage(id = "1", breedId = "1", url = "url1"),
            BreedImage(id = "2", breedId = "1", url = "url2")
        )
        appDatabase.breedDao().insertAll(breedList)
        appDatabase.breedImageDao().insertAll(breedImages)

        // Act
        val images = breedRepository.allImagesForBreed("1")

        // Assert
        assertEquals(2, images.size)
    }

    @Test
    fun getImageById_returnsCorrectImage() = runTest {
        // Arrange
        val breedList = listOf(
            Breed(id = "1", name = "Breed1"),
            Breed(id = "2", name = "Breed2")
        )
        val breedImage = BreedImage(id = "1", breedId = "1", url = "url1")
        appDatabase.breedDao().insertAll(breedList)
        appDatabase.breedImageDao().insertAll(listOf(breedImage))

        // Act
        val image = breedRepository.getImageById("1")

        // Assert
        assertNotNull(image)
        assertEquals(breedImage.url, image.url)
    }

    @Test
    fun getById_returnsCorrectBreed() = runTest {
        // Arrange
        val breedDb = Breed(id = "1", name = "Breed1")
        appDatabase.breedDao().insert(breedDb)

        // Act
        val breed = breedRepository.getById("1")

        // Assert
        assertNotNull(breed)
        assertEquals(breedDb.id, breed?.id)
    }

}