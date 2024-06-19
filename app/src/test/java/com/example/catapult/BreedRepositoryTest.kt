package com.example.catapult

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.catapult.api.BreedsApi
import com.example.catapult.coroutines.CoroutinesTestRule
import com.example.catapult.database.AppDatabase
import com.example.catapult.model.catalog.UIBreed
import com.example.catapult.repository.BreedRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BreedRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var breedRepository: BreedRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setup() {
        val context = mockk<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val breedsApi = mockk<BreedsApi>(relaxed = true)
        breedRepository = BreedRepository(breedsApi, database, mockk(relaxed = true))
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun fetchBreedDetails_fetchesCorrectBreedDetails() = runTest {
        val expectedBreed = UIBreed("1", "Breed1")
        coEvery { breedRepository.fetchBreedDetails("1") } returns expectedBreed

        val actualBreed = breedRepository.fetchBreedDetails("1")

        assertEquals(expectedBreed, actualBreed)
    }

}