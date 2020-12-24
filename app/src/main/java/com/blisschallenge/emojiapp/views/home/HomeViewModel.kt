package com.blisschallenge.emojiapp.views.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.helpers.DataState
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.repositories.GithubRepository
import kotlin.random.Random

class HomeViewModel @ViewModelInject constructor(
    private val repository: GithubRepository
): ViewModel() {

    lateinit var navController: NavController

    val items: LiveData<List<Emoji>>
        get() = this.repository.items

    val emoji: LiveData<Emoji>
        get() = _emoji

    val dataState: LiveData<DataState>
        get() = repository.dataState

    private val _emoji = MutableLiveData<Emoji>()

    fun fetchEmoji() {

        if (repository.isDataEmpty) {

            repository.emojis(viewModelScope) {
                _emoji.value = randomEmoji()
            }
        } else {
            _emoji.value = randomEmoji()
        }

    }

    fun randomEmoji(): Emoji? {
        val index = Random.nextInt(0, items.value!!.size - 1)
        return items.value?.get(index)
    }

    fun showList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_emojis_list_fragment)
        }
    }

    fun drawEmoji() {

        fetchEmoji()

    }
}