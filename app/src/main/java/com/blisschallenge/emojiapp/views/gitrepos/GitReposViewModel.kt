package com.blisschallenge.emojiapp.views.gitrepos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.Repo
import com.blisschallenge.emojiapp.models.entities.TextData
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository

class GitReposViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val dataState: LiveData<RequestInfo<List<Repo>>?>
        get() = this.profileRepository.dataState as MutableLiveData<RequestInfo<List<Repo>>?>

    val itemsValues: LiveData<MutableList<TextData>> = Transformations.map(dataState) {
        it?.data?.map { repo -> TextData(id = repo.name, value = repo.fullName) }?.toMutableList()
    }

    val profileName = MutableLiveData<String>()

    fun fetchData(profileName: String?, onFinish: (MutableLiveData<RequestInfo<List<Repo>>>) -> Unit = {}) {

        profileRepository.gitRepositories(viewModelScope, name = profileName, onFinish)
    }
}