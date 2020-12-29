package com.blisschallenge.emojiapp.models.entities

data class TextData(
    val id: String,
    val value: String
) {
    override fun equals(other: Any?) = id == (other as? TextData)?.id
}
