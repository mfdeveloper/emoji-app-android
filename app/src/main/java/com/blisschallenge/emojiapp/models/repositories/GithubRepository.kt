package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GitHubService,
    private val localDataSource: GithubDao
) : BaseRepository<List<Emoji>>() {

    fun emojis(modelScope: CoroutineScope, onFinish: (MutableLiveData<List<Emoji>>) -> Unit) {

        cacheOrRemoteRequest(
                modelScope,
                dbRequest = localDataSource::listEmojis,
                dbCacheSave = localDataSource::insertEmojis,
                remoteRequest = service::listEmojis,
                onFinish = onFinish
        )
    }
}