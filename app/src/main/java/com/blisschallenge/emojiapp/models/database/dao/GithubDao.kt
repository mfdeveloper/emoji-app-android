package com.blisschallenge.emojiapp.models.database.dao

import androidx.room.*
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.ProfileWithRepos
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

    @Transaction
    @Query("SELECT * FROM profiles WHERE login = :name")
    suspend fun listProfileRepos(name: String): List<ProfileWithRepos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)
}