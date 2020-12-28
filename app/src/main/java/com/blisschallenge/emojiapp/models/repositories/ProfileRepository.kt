package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val service: GitHubService,
    private val localDataSource: GithubDao
) : BaseRepository() {

    fun profile(modelScope: CoroutineScope, name: String?, onFinish: (MutableLiveData<RequestInfo<ProfileInfo>>) -> Unit = {}) {

        cacheOrRemoteRequest(
                modelScope,
                dbRequest = { localDataSource.findProfile(name = name) },
                dbCacheSave = localDataSource::insertProfile,
                remoteRequest = { service.userProfile(name = name!!) },
                onFinish = onFinish
        )
    }

    fun removeProfile(modelScope: CoroutineScope, profile: ProfileInfo, onFinish: (Int) -> Unit = {}) {

        modelScope.launch(Dispatchers.IO) {

            val result = localDataSource.deleteProfiles(profile)
            withContext(Dispatchers.Main) {
                onFinish(result)
            }
        }
    }

    fun avatars(modelScope: CoroutineScope, onFinish: (MutableLiveData<RequestInfo<List<ProfileInfo>>>) -> Unit = {}) {

        cacheOrRemoteRequest(
            modelScope,
            dbRequest = localDataSource::listProfiles,
            onFinish = onFinish
        )
    }

    fun gitRepositories(modelScope: CoroutineScope, name: String?, onFinish: (MutableLiveData<RequestInfo<List<Repo>>>) -> Unit = {}) {

        cacheOrRemoteRequest(
            modelScope,
            dbRequest = { localDataSource.listProfileRepos(name = name!!) },
            dbCacheSave = localDataSource::insertRepos,
            remoteRequest = { service.listRepositories(name = name!!) },
            onFinish = onFinish
        )
    }
}