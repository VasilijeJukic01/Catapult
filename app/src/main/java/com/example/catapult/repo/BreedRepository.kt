package com.example.catapult.repo

import com.example.catapult.api.BreedsApi
import com.example.catapult.api.networking.retrofit
import com.example.catapult.database.CatapultDatabase
import com.example.catapult.model.catalog.ViewBreed
import com.example.catapult.model.mappers.asBreedDbModel
import com.example.catapult.model.mappers.asViewBreed

object BreedRepository {

    private val database by lazy { CatapultDatabase.database }

    private val breedsApi: BreedsApi by lazy { retrofit.create(BreedsApi::class.java) }

    // Flow
    suspend fun fetchAllBreeds() {
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertAll(list = breeds.map { it.asBreedDbModel() })
    }

    fun observeAllBreeds() = database.breedDao().observeAll()

    fun fetchBreedDetails(breedId: String): ViewBreed? {
        return database.breedDao().getBreedById(breedId)?.asViewBreed()
    }

    // CRUD
    fun allBreeds() : List<ViewBreed> = database.breedDao().getAll().map { it.asViewBreed() }

    fun getById(id: String) : ViewBreed? {
        return database.breedDao().getBreedById(id)?.asViewBreed()
    }

}