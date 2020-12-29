package com.blisschallenge.emojiapp.models.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.database.AppDatabase
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.repositories.mediators.GithubReposMediator
import com.blisschallenge.emojiapp.models.api.GitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val db: AppDatabase,
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

    @Deprecated(
        message = "Replaced by pagingGitRepos to fetch big data with jetpack Paging 3 library",
        replaceWith = ReplaceWith("pagingGitRepos"),
        level = DeprecationLevel.WARNING
    )
    fun gitRepositories(modelScope: CoroutineScope, name: String?, onFinish: (MutableLiveData<RequestInfo<List<Repo>>>) -> Unit = {}) {

        cacheOrRemoteRequest(
            modelScope,
            dbRequest = { localDataSource.listReposProfile(name = name!!)},
            dbCacheSave = localDataSource::insertRepos,
            remoteRequest = { service.listRepositories(name = name!!) },
            onFinish = onFinish
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    fun pagingGitRepos(name: String?): Flow<PagingData<Repo>> {
        Log.d("ProfileRepository", "New query: $name")

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubReposMediator(name!!, db, networkService = service),
            pagingSourceFactory = { localDataSource.pagingSource(name) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}