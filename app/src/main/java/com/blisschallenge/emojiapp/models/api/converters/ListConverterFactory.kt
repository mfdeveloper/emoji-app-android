package com.blisschallenge.emojiapp.models.api.converters

import com.blisschallenge.emojiapp.models.entities.ConvertableEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ListConverterFactory : JsonDeserializer<List<ConvertableEntity>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<ConvertableEntity> {

        return if (json?.isJsonObject == true) {

            val emojiRepoConverter = EmojiConverterFactory()
            emojiRepoConverter.deserialize(json, typeOfT, context)

        } else {
            val gitRepoConverter = GitRepoConverterFactory()
            gitRepoConverter.deserialize(json, typeOfT, context)
        }
    }
}