package com.blisschallenge.emojiapp.views.avatars

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.ImageData
import com.blisschallenge.emojiapp.models.entities.ProfileInfo
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository

class AvatarsListViewModel @ViewModelInject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val dataState: LiveData<RequestInfo<List<ProfileInfo>>?>
        get() = this.profileRepository.dataState as MutableLiveData<RequestInfo<List<ProfileInfo>>?>

    val itemsUrls: LiveData<MutableList<ImageData>> = Transformations.map(dataState) {
        it?.data?.map { profile -> ImageData(id = profile.login, url = profile.avatar_url) }?.toMutableList()
    }

    init {
        fetchData()
    }

    fun fetchData(onFinish: (MutableLiveData<RequestInfo<List<ProfileInfo>>>) -> Unit = {}) {

        profileRepository.avatars(viewModelScope, onFinish)

    }

    fun remove(profile: ProfileInfo) {
        profileRepository.removeProfile(viewModelScope, profile)
    }
}