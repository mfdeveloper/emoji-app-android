package com.blisschallenge.emojiapp.models.services

import com.blisschallenge.emojiapp.models.entities.Emoji
import retrofit2.Response
import retrofit2.http.GET

interface GitHubService {

    @GET("emojis")
    suspend fun listEmojis(): Response<List<Emoji>>
}