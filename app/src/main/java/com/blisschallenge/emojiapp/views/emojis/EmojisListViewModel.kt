package com.blisschallenge.emojiapp.views.emojis

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ImageData
import com.blisschallenge.emojiapp.models.repositories.EmojisRepository

class EmojisListViewModel @ViewModelInject constructor(
    private val emojiRepository: EmojisRepository
) : ViewModel() {

    val dataState: LiveData<RequestInfo<List<Emoji>>?>
        get() = this.emojiRepository.dataState as MutableLiveData<RequestInfo<List<Emoji>>?>

    val itemsUrls: LiveData<MutableList<ImageData>> = Transformations.map(dataState) {
        it?.data?.map { emoji -> ImageData(id = emoji.name, url = emoji.url) }?.toMutableList()
    }

    init {

        fetchData()
    }

    fun fetchData(onFinish: (MutableLiveData<RequestInfo<List<Emoji>>>) -> Unit = {}) {

        emojiRepository.emojis(viewModelScope, onFinish)
    }
}