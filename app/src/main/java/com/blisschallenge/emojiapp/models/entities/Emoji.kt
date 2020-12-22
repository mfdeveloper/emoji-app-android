package com.blisschallenge.emojiapp.models.entities

import com.google.gson.annotations.SerializedName

data class Emoji(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    var url: String = ""
) {
    override fun equals(other: Any?): Boolean = name == (other as? Emoji)?.name
}
