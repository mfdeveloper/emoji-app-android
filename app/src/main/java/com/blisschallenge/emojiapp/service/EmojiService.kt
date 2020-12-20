package com.blisschallenge.emojiapp.service

import com.blisschallenge.emojiapp.model.Emoji
import retrofit2.Response
import retrofit2.http.GET

interface EmojiService {

    @GET()
    suspend fun all(): Response<List<Emoji>>
}