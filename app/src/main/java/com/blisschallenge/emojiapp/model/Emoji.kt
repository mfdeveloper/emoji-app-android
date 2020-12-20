package com.blisschallenge.emojiapp.model

import com.google.gson.annotations.SerializedName

data class Emoji(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
