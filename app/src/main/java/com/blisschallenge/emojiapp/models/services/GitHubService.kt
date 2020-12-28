package com.blisschallenge.emojiapp.models.services

import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("emojis")
    suspend fun listEmojis(): Response<List<Emoji>>

    @GET("users/{username}")
    suspend fun userProfile(@Path("username") name: String): Response<ProfileInfo>
}