package com.blisschallenge.emojiapp.models.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithRepos(
    @Embedded
    val profile: ProfileInfo?,
    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val repos: List<Repo>
)
