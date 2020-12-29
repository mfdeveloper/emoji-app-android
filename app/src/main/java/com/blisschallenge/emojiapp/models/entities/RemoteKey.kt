package com.blisschallenge.emojiapp.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey @JvmOverloads constructor(
    @PrimaryKey
    val repoId: String,
    val label: String,
    val prevKey: Int? = null,
    val nextKey: Int? = null
)
