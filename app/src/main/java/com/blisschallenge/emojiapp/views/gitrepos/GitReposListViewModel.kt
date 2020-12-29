package com.blisschallenge.emojiapp.views.gitrepos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GitReposListViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Repo>>? = null


    fun searchReposBy(profileName: String?): Flow<PagingData<Repo>> {
        val lastResult = currentSearchResult

        if (profileName == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = profileName

       val newResult = profileRepository.pagingGitRepos(name = profileName)
            .cachedIn(viewModelScope)

        currentSearchResult = newResult

        return newResult
    }
}