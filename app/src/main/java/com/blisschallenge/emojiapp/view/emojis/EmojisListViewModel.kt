package com.blisschallenge.emojiapp.view.emojis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmojisListViewModel : ViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "This is a list Fragment"
    }
}