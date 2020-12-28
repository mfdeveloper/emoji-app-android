package com.blisschallenge.emojiapp.views.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.helpers.RequestInfo
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.repositories.EmojisRepository
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository
import kotlin.random.Random

class HomeViewModel @ViewModelInject constructor(
    private val emojiRepository: EmojisRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {

    lateinit var navController: NavController

    val emoji: LiveData<Emoji>
        get() = _emoji

    val dataState: LiveData<RequestInfo<List<Emoji>>?>
        get() = this.emojiRepository.dataState as MutableLiveData<RequestInfo<List<Emoji>>?>

    val profileName = MutableLiveData<String>()

    private val _emoji = MutableLiveData<Emoji>()

    fun fetchEmoji() {

        if (dataState.value?.isDataEmpty == true) {

            emojiRepository.emojis(viewModelScope) {
                _emoji.value = randomEmoji()
            }
        } else {
            _emoji.value = randomEmoji()
        }
    }

    fun randomEmoji(): Emoji? {
        val index = Random.nextInt(0, dataState.value?.data!!.size - 1)
        return dataState.value?.data?.get(index)
    }

    fun showList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_emojis_list_fragment)
        }
    }

    fun showAvatarsList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_avatars_list_fragment)
        }
    }

    fun drawEmoji() {

        fetchEmoji()
    }

    fun searchProfile() {

        if (!profileName.value.isNullOrBlank()) {

            profileRepository.profile(viewModelScope, name = profileName.value)
        }
    }
}