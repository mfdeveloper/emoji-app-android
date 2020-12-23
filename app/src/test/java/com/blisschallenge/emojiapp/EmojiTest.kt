package com.blisschallenge.emojiapp

import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.converters.EmojiConverterFactory
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

/**
 * Emoji data testing
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * @author Felipe Ferreira
 */
class EmojiTest {

    private val jsonData = JsonObject()

    @Before
    fun setUp() {

        jsonData.add("1st_place_medal", JsonPrimitive("https://github.githubassets.com/images/icons/emoji/unicode/1f947.png?v8"))
        jsonData.add("2nd_place_medal", JsonPrimitive("https://github.githubassets.com/images/icons/emoji/unicode/1f948.png?v8"))
    }

    @Test
    fun convertDeserializedData() {

        val converter = EmojiConverterFactory()
        val type = mockk<Type>()
        val context = mockk<JsonDeserializationContext>()

        val data = converter.deserialize(jsonData, type, context)

        assertThat(data).contains(Emoji(name = "1st_place_medal"))
    }
}