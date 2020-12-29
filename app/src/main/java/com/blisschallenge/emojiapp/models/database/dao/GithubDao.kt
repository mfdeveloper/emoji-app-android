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

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM git_repositories as repo INNER JOIN profiles ON repo.profileId = profiles.id WHERE login = :name")
    suspend fun listProfileRepos(name: String): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)
}