package com.example.catapult.api.networking

import kotlinx.serialization.json.Json

val AppJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}
