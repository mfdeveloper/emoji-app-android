package com.blisschallenge.emojiapp.models.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo

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
    suspend fun insertProfile(emojis: ProfileInfo)
}