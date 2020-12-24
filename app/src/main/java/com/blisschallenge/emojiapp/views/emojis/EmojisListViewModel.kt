package com.blisschallenge.emojiapp.views.emojis

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.repositories.GithubRepository

class EmojisListViewModel @ViewModelInject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    val items: LiveData<List<Emoji>>
        get() = repository.items

    val itemsUrls: LiveData<MutableList<String>> = Transformations.map(items) {
        it.map { emoji -> emoji.url }.toMutableList()
    }

    init {
        fetchData()
    }

    fun fetchData() {

        repository.emojis(viewModelScope)
    }
}