package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class EmojisRepository @Inject constructor(
    private val service: GitHubService,
    private val localDataSource: GithubDao
) : BaseRepository() {

    fun emojis(modelScope: CoroutineScope, onFinish: (MutableLiveData<RequestInfo<List<Emoji>>>) -> Unit) {

        cacheOrRemoteRequest(
                modelScope,
                dbRequest = localDataSource::listEmojis,
                dbCacheSave = localDataSource::insertEmojis,
                remoteRequest = service::listEmojis,
                onFinish = onFinish
        )
    }
}