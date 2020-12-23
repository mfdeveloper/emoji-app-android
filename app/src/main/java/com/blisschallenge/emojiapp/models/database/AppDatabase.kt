package com.blisschallenge.emojiapp.models.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.Emoji

@Database(entities = [Emoji::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun githubDao(): GithubDao

    companion object {
        const val DATABASE_NAME = "github-services"
    }
}