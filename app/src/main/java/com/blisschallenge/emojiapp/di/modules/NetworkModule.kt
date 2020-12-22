package com.blisschallenge.emojiapp.di.modules

import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.GitHubService
import com.blisschallenge.emojiapp.models.services.converter.EmojiConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object NetworkModule {

    private val baseUrl get() = "https://api.github.com"

    @Provides
    @Singleton
    fun provideGson(): Gson {

        return GsonBuilder()
            .registerTypeAdapter(Emoji::class.java, EmojiConverterFactory())
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
    @Singleton
    fun provideGitHubService (retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }
}