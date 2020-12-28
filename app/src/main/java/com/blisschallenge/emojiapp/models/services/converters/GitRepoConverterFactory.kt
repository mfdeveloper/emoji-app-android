package com.blisschallenge.emojiapp.models.services.converters

import com.blisschallenge.emojiapp.models.entities.ProfileWithRepos
import com.blisschallenge.emojiapp.models.entities.Repo
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GitRepoConverterFactory : JsonDeserializer<List<ProfileWithRepos>> {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): List<ProfileWithRepos> {

        val jsonArr = json?.asJsonArray
        val repos = mutableListOf<Repo>()
        var profilesAndRepos = mutableListOf<ProfileWithRepos>()

        jsonArr.let {
            it?.onEach { jsonElement ->
                val jsonRepo = jsonElement?.asJsonObject

                val gitRepo = gson.fromJson(jsonRepo, Repo::class.java)
                gitRepo.profileId = gitRepo.owner?.id

                repos.add(gitRepo)
                profilesAndRepos.add(
                    ProfileWithRepos(
                        profile = gitRepo.owner,
                        repos = repos
                    )
                )
            }

            return profilesAndRepos
        }
    }
}