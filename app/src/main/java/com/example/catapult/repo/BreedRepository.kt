package com.example.catapult.repo

import com.example.catapult.api.BreedApiModel
import com.example.catapult.api.BreedsApi
import com.example.catapult.api.networking.retrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object BreedRepository {

    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)
    private val breedList = MutableStateFlow(listOf<BreedApiModel>())

    // Flow
    suspend fun fetchAllBreeds() {
        val breeds = breedsApi.getAllBreeds()
        breedList.update { breeds }
    }

    fun fetchBreedDetails(breedId: String): BreedApiModel? {
        return breedList.value.find {
            it.id == breedId
        }
    }

    // CRUD
    fun allBreeds() : List<BreedApiModel> = breedList.value

    fun getById(id: String) : BreedApiModel? {
        return breedList.value.find {
            it.id == id
        }
    }

}