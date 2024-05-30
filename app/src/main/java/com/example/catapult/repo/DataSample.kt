package com.example.catapult.repo

import com.example.catapult.model.catalog.ViewBreed
import com.example.catapult.model.catalog.Characteristics

val DataSample = listOf(
    ViewBreed(
        id = "1",
        name = "Maine Coon",
        altNames = listOf("Coon Cat", "Maine Cat"),
        description = "The Maine Coon is a large and sociable cat, hence its nickname, 'the gentle giant.'",
        temperament = listOf("Intelligent", "Gentle", "Relaxed", "Playful"),
        origin = "United States",
        weight = "9-18 pounds",
        lifeSpan = "9-15 years",
        rare = 0,
        characteristics = Characteristics(
            adaptability = 5,
            affectionLevel = 5,
            energyLevel = 3,
            intelligence = 5,
            strangerFriendly = 3
        ),
        wikipediaUrl = "https://en.wikipedia.org/wiki/Maine_Coon",
        imageUrl = ""
    ),
    ViewBreed(
        id = "2",
        name = "Siamese",
        altNames = listOf("Siam"),
        description = "The Siamese cat is one of the first distinctly recognized breeds of Asian cat.",
        temperament = listOf("Active", "Playful", "Intelligent", "Affectionate"),
        origin = "Thailand",
        weight = "8-15 pounds",
        lifeSpan = "10-15 years",
        rare = 0,
        characteristics = Characteristics(
            adaptability = 5,
            affectionLevel = 5,
            energyLevel = 4,
            intelligence = 5,
            strangerFriendly = 3
        ),
        wikipediaUrl = "https://en.wikipedia.org/wiki/Siamese_cat",
        imageUrl = ""
    )
)