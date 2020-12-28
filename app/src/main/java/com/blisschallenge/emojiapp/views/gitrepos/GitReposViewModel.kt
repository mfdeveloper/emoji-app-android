package com.blisschallenge.emojiapp.views.gitrepos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.ProfileWithRepos
import com.blisschallenge.emojiapp.models.entities.TextData
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository

class GitReposViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val dataState: LiveData<RequestInfo<List<ProfileWithRepos>>?>
        get() = this.profileRepository.dataState as MutableLiveData<RequestInfo<List<ProfileWithRepos>>?>

    val itemsValues: LiveData<MutableList<TextData>> = Transformations.map(dataState) {
        it?.data?.flatMap { profileRepos ->
            profileRepos.repos.map { repo -> TextData(id = repo.name, value = repo.fullName) }
        }?.toMutableList()
    }

    init {

        fetchData()
    }

    fun fetchData(onFinish: (MutableLiveData<RequestInfo<List<ProfileWithRepos>>>) -> Unit = {}) {

        profileRepository.gitRepositories(viewModelScope, "mfdeveloper", onFinish)
    }
}