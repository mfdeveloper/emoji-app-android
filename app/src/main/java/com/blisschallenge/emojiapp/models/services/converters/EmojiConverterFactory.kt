package com.blisschallenge.emojiapp.models.services.converters

import com.blisschallenge.emojiapp.models.entities.Emoji
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

        val originData = json?.asJsonObject!!

        return originData.entrySet().map {
            Emoji(name = it.key, url = it.value.asString)
        }
    }
}