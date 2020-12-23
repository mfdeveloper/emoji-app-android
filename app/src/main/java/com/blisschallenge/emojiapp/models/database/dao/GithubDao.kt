package com.blisschallenge.emojiapp.models.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blisschallenge.emojiapp.models.entities.Emoji

@Dao
interface GithubDao {

    @Query("SELECT * FROM emojis")
    suspend fun listEmojis(): List<Emoji>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojis(emojis: List<Emoji>)
}