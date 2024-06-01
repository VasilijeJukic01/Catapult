package com.example.catapult.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
    val avatarUrl: String,
) {
    init {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        require(email.matches(emailRegex)) { "Invalid email format" }
        require(firstName.isNotBlank()) { "First name cannot be blank or empty" }
        require(lastName.isNotBlank()) { "Last name cannot be blank or empty" }

        val nicknameRegex = "^[A-Za-z0-9_]+$".toRegex()
        require(nickname.matches(nicknameRegex)) { "Invalid nickname format" }
    }

    val fullName: String
        get() = "$firstName $lastName"
}