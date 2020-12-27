package com.blisschallenge.emojiapp.di.modules

import android.content.Context
import androidx.room.Room
import com.blisschallenge.emojiapp.models.database.AppDatabase
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.repositories.EmojisRepository
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository
import com.blisschallenge.emojiapp.models.services.GitHubService
import com.blisschallenge.emojiapp.models.services.converters.EmojiConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val baseUrl = "https://api.github.com"

    @Provides
    @Singleton
    fun provideGson(): Gson {

        return GsonBuilder()
            .registerTypeAdapter(List::class.java, EmojiConverterFactory())
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideGitHubService (retrofit: Retrofit): GitHubService = retrofit.create(GitHubService::class.java)


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {

        if (AppDatabase.CLEAR_FIRST) {
            appContext.deleteDatabase(AppDatabase.DATABASE_NAME)
        }

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
         .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.githubDao()

    @Singleton
    @Provides
    fun provideRepository(service: GitHubService, dao: GithubDao) = EmojisRepository(service, dao)

    @Singleton
    @Provides
    fun provideProfileRepository(service: GitHubService, dao: GithubDao) = ProfileRepository(service, dao)

}