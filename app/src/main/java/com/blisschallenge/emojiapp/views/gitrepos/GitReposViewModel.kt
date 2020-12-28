package com.blisschallenge.emojiapp.views.gitrepos

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.ProfileWithRepos
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository

class GitReposViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gitrepos Fragment"
    }
    val text: LiveData<String> = _text

    init {

        fetchData()
    }

    fun fetchData(onFinish: (MutableLiveData<RequestInfo<List<ProfileWithRepos>>>) -> Unit = {}) {

        profileRepository.gitRepositories(viewModelScope, "mfdeveloper", onFinish)
    }
}