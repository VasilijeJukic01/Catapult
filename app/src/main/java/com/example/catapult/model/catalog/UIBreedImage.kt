package com.example.catapult.model.catalog

data class UIBreedImage(
    val id: String = "",
    val breedId: String = "",
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UIBreedImage

        return url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}


