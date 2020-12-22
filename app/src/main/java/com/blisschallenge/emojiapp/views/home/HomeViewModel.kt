package com.blisschallenge.emojiapp.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    @Inject lateinit var service: GitHubService
    lateinit var navController: NavController

    val text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val items: LiveData<List<Emoji>>
        get() = _items

    private val _items = MutableLiveData<List<Emoji>>()

    fun fetchEmojis() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = service.listEmojis()

            if (response.isSuccessful) {

                withContext(Dispatchers.Main) {
                    _items.value = response.body()
                }
            }
        }
    }

    fun showList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_emojis_list_fragment)
        }
    }
}