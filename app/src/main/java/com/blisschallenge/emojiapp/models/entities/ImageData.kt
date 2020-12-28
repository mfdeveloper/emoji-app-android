package com.blisschallenge.emojiapp.models.entities

data class ImageData(
    var id: String?,
    val url: String
)
{
    override fun equals(other: Any?) = url == (other as? ImageData)?.url
}
