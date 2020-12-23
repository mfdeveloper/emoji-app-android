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
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class HomeViewModel @ViewModelInject constructor(
    private val service: GitHubService
): ViewModel() {

    lateinit var navController: NavController

    val text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val items: LiveData<List<Emoji>>
        get() = _items

    val emoji: LiveData<Emoji>
        get() = _emoji

    val dataState: LiveData<DataState>
        get() = _dataState

    private val _emoji = MutableLiveData<Emoji>()
    private val _items = MutableLiveData<List<Emoji>>()
    private val _dataState = MutableLiveData(DataState.NONE)

    fun fetchEmojis() {

        val dataIsEmpty = _items.value?.isEmpty() ?: true

        if(dataIsEmpty) {

            _dataState.value = DataState.START

            viewModelScope.launch(Dispatchers.IO) {
                val response = service.listEmojis()

                if (response.isSuccessful) {

                    withContext(Dispatchers.Main) {
                        _items.value = response.body()
                        _emoji.value = randomEmoji()

                        _dataState.value = DataState.LOADED
                    }
                }
            }
        } else {
            _emoji.value = randomEmoji()
        }
    }

    fun randomEmoji(): Emoji? {
        val index = Random.nextInt(0, _items.value!!.size - 1)
        return _items.value?.get(index)
    }

    fun showList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_emojis_list_fragment)
        }
    }

    fun drawEmoji() {

         fetchEmojis()
    }
}