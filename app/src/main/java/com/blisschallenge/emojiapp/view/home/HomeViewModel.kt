package com.blisschallenge.emojiapp.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.blisschallenge.emojiapp.R

class HomeViewModel : ViewModel() {

    lateinit var navController: NavController

    val text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    fun showList() {

        if (::navController.isInitialized) {
            navController.navigate(R.id.action_home_fragment_to_emojis_list_fragment)
        }
    }
}