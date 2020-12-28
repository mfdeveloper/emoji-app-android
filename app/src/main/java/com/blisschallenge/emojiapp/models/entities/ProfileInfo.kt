package com.blisschallenge.emojiapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity(tableName = "profiles")
data class ProfileInfo(
    @PrimaryKey(autoGenerate = true)
    val uuid: Int? = null,
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatar_url: String
)
