package com.blisschallenge.emojiapp.models.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName

/**
 * See about [Room @Ignore problems](https://github.com/android/architecture-components-samples/issues/421)
 *
 * See also [Kotlin Overloads generation](https://kotlinlang.org/docs/reference/java-to-kotlin-interop.html#overloads-generation)
 */
@Entity(
    tableName = "git_repositories",
    foreignKeys = [
        ForeignKey(
            entity = ProfileInfo::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Repo(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @ColumnInfo(defaultValue = "NULL", index = true)
    var profileId: String? = null,
    @SerializedName("name")
    var name: String,
    @SerializedName("full_name")
    @ColumnInfo(name = "full_name")
    var fullName: String,
    @SerializedName("description")
    var description: String?,
    @SerializedName("language")
    var language: String?,
    @SerializedName("stargazers_count")
    var stars: String,
    @SerializedName("forks_count")
    var forks: String,
    @SerializedName("private")
    @ColumnInfo(name = "private_access")
    val privateAccess: Boolean = false,
    @Ignore
    val owner: ProfileInfo? = null
) : ConvertableEntity {
    constructor(
            id: String,
            profileId: String?,
            name: String,
            fullName: String,
            description: String?,
            language: String?,
            stars: String,
            forks: String,
            privateAccess: Boolean
    ) : this(
            id,
            profileId,
            name,
            fullName,
            description,
            language,
            stars,
            forks,
            privateAccess,
            null
    )

    override fun equals(other: Any?): Boolean {
        val otherRepo = other as? Repo
        return id == otherRepo?.id && name == otherRepo.name
    }
}
