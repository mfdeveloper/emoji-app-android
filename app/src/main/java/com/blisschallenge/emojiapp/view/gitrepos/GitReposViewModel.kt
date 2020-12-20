package com.blisschallenge.emojiapp.view.gitrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GitReposViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gitrepos Fragment"
    }
    val text: LiveData<String> = _text
}