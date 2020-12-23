package com.blisschallenge.emojiapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "emojis")
data class Emoji(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("name")
    val name: String
) {
    override fun equals(other: Any?): Boolean = name == (other as? Emoji)?.name
}
