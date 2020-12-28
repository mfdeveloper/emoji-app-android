package com.blisschallenge.emojiapp

import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.services.converters.EmojiConverterFactory
import com.blisschallenge.emojiapp.models.services.converters.GitRepoConverterFactory
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Type

/**
 * Gson converters data testing
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * See also [Android Studio unit testing: read data (input) file](https://stackoverflow.com/questions/29341744/android-studio-unit-testing-read-data-input-file)
 * @author Felipe Ferreira
 */
class GsonConvertersTest {

    private val jsonData = JsonObject()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    // Mocks
    @MockK
    private lateinit var type: Type

    @MockK
    private lateinit var context: JsonDeserializationContext

    @Before
    fun setUp() {

        MockKAnnotations.init(this)

        jsonData.add("1st_place_medal", JsonPrimitive("https://github.githubassets.com/images/icons/emoji/unicode/1f947.png?v8"))
        jsonData.add("2nd_place_medal", JsonPrimitive("https://github.githubassets.com/images/icons/emoji/unicode/1f948.png?v8"))
    }

    @Test
    fun convertJsonElementToEmojisList() {

        val converter = EmojiConverterFactory()
        val data = converter.deserialize(jsonData, type, context)

        assertThat(data).contains(Emoji(name = "1st_place_medal"))
    }

    @Test
    fun convertJsonArrayToProfileAndReposList() {

        val jsonRepos = ClassLoader.getSystemResource("github_profile_repos.json")?.readText()
        val reposType = object : TypeToken<List<Repo>>() {}.type

        val repos = gson.fromJson<List<Repo>>(jsonRepos, reposType)

        val jsonElRepos = gson.toJsonTree(repos)

        val converter = GitRepoConverterFactory()
        val data = converter.deserialize(jsonElRepos, type, context)

        assertThat(data).containsAnyIn(repos)
//        assertThat(data.first().repos).containsAnyIn(repos)
    }
}