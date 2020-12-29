package com.blisschallenge.emojiapp.models.repositories.mediators

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.blisschallenge.emojiapp.models.database.AppDatabase
import com.blisschallenge.emojiapp.models.entities.RemoteKey
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.api.GitHubService
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class GithubReposMediator(
    private val query: String,
    private val db: AppDatabase?,
    private val networkService: GitHubService?
) : RemoteMediator<Int, Repo>() {


    private val keysStrategy = RemoteKeyStrategy.STARTING_INDEX
    private val githubDao = db?.githubDao()
    private val remoteKeyDao = db?.remoteKeyDao()

    enum class RemoteKeyStrategy {
        /**
         * Get the prev/next keys "automatically" from Github header link
         * from the API rest request
         */
        HEADER_LINK,

        /**
         * Get the prev/next keys by convention:
         * (+1 or -1) from the current page
         */
        STARTING_INDEX
    }

    constructor(query: String) : this(query, null, null)

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        Log.d("GithubReposMediator", "New query: $query")

        return try {

            val pageIndex = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    if (remoteKey == null) {
                        // The LoadType is PREPEND so some data was loaded before,
                        // so we should have been able to get remote keys
                        // If the remoteKeys are null, then we're an invalid state and we have a bug
                        throw InvalidObjectException("Remote key and the prevKey should not be null")
                    }

                    remoteKey.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)

                    remoteKeys?.nextKey ?: throw InvalidObjectException("Remote key and the nextKey should not be null for $loadType")
                }
            }

            val response = networkService?.listRepositories(
                    name = query,
                    page = pageIndex,
                    perPage = state.config.pageSize
            )

            val items = response?.body()
            val endOfPagination = items?.isEmpty() == true

            val linkHeader = response?.headers()?.get("link")
            var keys = mutableMapOf<String, Int?>()

            if (keysStrategy == RemoteKeyStrategy.STARTING_INDEX) {
                keys["prevKey"] = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1
                keys["nextKey"] = if (endOfPagination) null else pageIndex + 1
            } else {
                keys = getHeaderKeys(linkHeader)
            }

            db?.withTransaction {
                if (loadType == LoadType.REFRESH) {

                    remoteKeyDao?.clearRemoteKeys()
                    githubDao?.deleteReposBy(profileName = query)
                }

                val keysToAdd = items?.map {
                    RemoteKey(repoId = it.id, label = query, prevKey = keys["prevKey"], nextKey = keys["nextKey"])
                }

                remoteKeyDao?.insertOrReplace(keysToAdd)

                githubDao?.insertRepos(items!!)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)

        }catch (err: IOException) {
            MediatorResult.Error(err)
        } catch (err: HttpException) {
            MediatorResult.Error(err)
        }
    }

    fun getHeaderKeys(header: String?): MutableMap<String, Int?> {

        val numberPattern = "([&?])page="
        val value = mutableMapOf<String, Int?>()

        if (header != null) {
            val regexStates = ";\\s+rel=\"*\"".toRegex()
            val matches = regexStates.split(header)

            val regexNumberKey = { input: String ->
                val matchInput = Regex("${numberPattern}\\d+").find(input)
                matchInput?.value?.replace(numberPattern.toRegex(), "")?.toInt()
            }

            matches.onEachIndexed { index, foundedValue ->
                if(foundedValue.contains("prev")) {

                    value["prevKey"] = regexNumberKey(matches[index - 1])
                }

                if (foundedValue.contains("next")) {
                    value["nextKey"] = regexNumberKey(matches[index - 1])
                }
            }
        }

        return value
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Repo>
    ): RemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->

                remoteKeyDao?.findRemoteKeyBy(repoId = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): RemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { repo ->

                    // Get the remote keys of the first items retrieved
                    remoteKeyDao?.findRemoteKeyBy(repoId = repo.id)
                }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): RemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { repo ->

                    // Get the remote keys of the last item retrieved
                    remoteKeyDao?.findRemoteKeyBy(repoId = repo.id)
                }
    }

    companion object {
        // GitHub page API is 1 based: https://developer.github.com/v3/#pagination
        const val STARTING_PAGE_INDEX = 1
    }
}