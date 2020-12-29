package com.blisschallenge.emojiapp.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * See about [Room @Ignore problems](https://github.com/android/architecture-components-samples/issues/421)
 */
@Entity(tableName = "git_repositories")
data class Repo(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @ColumnInfo(defaultValue = "NULL")
    var profileId: String? = null,
    @SerializedName("name")
    var name: String,
    @SerializedName("full_name")
    @ColumnInfo(name = "full_name")
    var fullName: String,
    @SerializedName("private")
    val privateAccess: Boolean = false,
    @Ignore
    val owner: ProfileInfo? = null
) : ConvertableEntity {
    constructor(
            id: String,
            profileId: String?,
            name: String,
            fullName: String,
            privateAccess: Boolean
    ) : this(
            id,
            profileId,
            name,
            fullName,
            privateAccess,
            null
    )

    override fun equals(other: Any?): Boolean {
        val otherRepo = other as? Repo
        return id == otherRepo?.id && name == otherRepo.name
    }
}
