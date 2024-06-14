package com.example.catapult.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val avatar: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val nickname: String = "",
    val email: String = "",
    val active: Int = 0,
)