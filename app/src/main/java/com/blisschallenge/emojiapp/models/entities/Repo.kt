package com.blisschallenge.emojiapp.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "git_repositories")
data class Repo(
    @PrimaryKey(autoGenerate = true)
    val uuid: Int? = null,
    @SerializedName("id")
    val id: String,
    @ColumnInfo(defaultValue = "NULL")
    val profileId: String? = null,
    @SerializedName("name")
    var name: String,
    @SerializedName("full_name")
    @ColumnInfo(name = "full_name")
    var fullName: String,
    @SerializedName("private")
    val privateAccess: Boolean = false
)
