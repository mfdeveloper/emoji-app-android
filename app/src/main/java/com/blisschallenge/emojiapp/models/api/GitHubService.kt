package com.blisschallenge.emojiapp.models.api

import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.Repo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @GET("emojis")
    suspend fun listEmojis(): Response<List<Emoji>>

    @GET("users/{username}")
    suspend fun userProfile(@Path("username") name: String): Response<ProfileInfo>

    @GET("users/{username}/repos")
    suspend fun listRepositories(
            @Path("username") name: String,
            @Query("page") page: Int? = 1,
            @Query("per_page") perPage: Int? = 20
    ): Response<List<Repo>>
}