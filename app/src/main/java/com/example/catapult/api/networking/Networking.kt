package com.example.catapult.api.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

val okHttpClient = OkHttpClient.Builder()
    // Custom header interceptor
    .addInterceptor {
        val updatedRequest = it.request().newBuilder()
            .addHeader("x-api-key", "live_xHucNym4M9fZLY6W6xSfTWpcs2dx4W1WZedHYBDmUFyQT0rgSXMbheVH6Bt4FlBu")
            .build()
        it.proceed(updatedRequest)
    }
    // Logging interceptor
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()