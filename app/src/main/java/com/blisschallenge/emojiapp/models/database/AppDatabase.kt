package com.blisschallenge.emojiapp.models.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.database.dao.RemoteKeyDao
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.RemoteKey
import com.blisschallenge.emojiapp.models.entities.Repo

@Database(entities = [Emoji::class, ProfileInfo::class, Repo::class, RemoteKey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun githubDao(): GithubDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "github-api.db"
        const val CLEAR_FIRST = false
    }
}