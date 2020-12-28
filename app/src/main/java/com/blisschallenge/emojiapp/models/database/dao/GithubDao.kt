package com.blisschallenge.emojiapp.models.database.dao

import androidx.room.*
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.Repo

@Dao
interface GithubDao {

    @Query("SELECT * FROM emojis ORDER BY id ASC")
    suspend fun listEmojis(): List<Emoji>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojis(emojis: List<Emoji>)

    @Query("SELECT * FROM profiles ORDER BY login ASC")
    suspend fun listProfiles(): List<ProfileInfo>

    @Query("SELECT * FROM profiles WHERE login = :name ORDER BY login ASC")
    suspend fun findProfile(name: String? = ""): ProfileInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileInfo)

    @Delete
    suspend fun deleteProfiles(vararg profiles: ProfileInfo): Int

    /**
     * Get all repositories that belongs to an profile.
     * That uses the [name] param to avoid perform additional query to use the profile ID
     *
     * Using the @[ForeignKey] annotation, that query can performs
     * a join between [ProfileInfo] and [Repo]
     *
     * @param[name] The [ProfileInfo] name to query repositories
     * @return The [List] of [Repo] instances
     */
    @Query("SELECT * FROM git_repositories as repo WHERE full_name LIKE :name || '/%'")
    suspend fun listReposProfile(name: String): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)
}