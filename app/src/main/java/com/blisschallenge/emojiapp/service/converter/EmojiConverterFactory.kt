package com.blisschallenge.emojiapp.service.converter

import com.blisschallenge.emojiapp.model.Emoji
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class EmojiConverterFactory : JsonDeserializer<List<Emoji>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Emoji> {
        TODO("Not yet implemented")
    }
}