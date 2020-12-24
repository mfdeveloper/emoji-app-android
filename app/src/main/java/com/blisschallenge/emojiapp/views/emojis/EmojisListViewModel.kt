package com.blisschallenge.emojiapp.views.emojis

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.helpers.DataState
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.entities.ImageData
import com.blisschallenge.emojiapp.models.repositories.GithubRepository

class EmojisListViewModel @ViewModelInject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    val items: LiveData<List<Emoji>>
        get() = repository.items
    val dataState: LiveData<DataState>
        get() = repository.dataState

    val itemsUrls: LiveData<MutableList<ImageData>> = Transformations.map(items) {
        it.map { emoji -> ImageData(id = emoji.name, url = emoji.url) }.toMutableList()
    }

    init {
        fetchData()
    }

    fun fetchData(onFinish: (MutableLiveData<List<Emoji>>) -> Unit = {}) {

        repository.emojis(viewModelScope, onFinish)
    }
}