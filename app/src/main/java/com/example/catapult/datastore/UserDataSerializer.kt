package com.example.catapult.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class UserDataSerializer : Serializer<List<UserData>> {

    override val defaultValue: List<UserData> = emptyList()

    override suspend fun readFrom(input: InputStream): List<UserData> {
        return withContext(Dispatchers.IO) {
            val inputString = input.bufferedReader().use { it.readText() }
            Json.decodeFromString(inputString)
        }
    }

    override suspend fun writeTo(t: List<UserData>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val outputString = Json.encodeToString(t)
            output.write(outputString.toByteArray())
        }
    }
}