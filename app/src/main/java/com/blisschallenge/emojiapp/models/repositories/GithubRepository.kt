package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.helpers.DataState
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.entities.Emoji
import com.blisschallenge.emojiapp.models.services.GitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GitHubService,
    private val localDataSource: GithubDao
) {

    val isDataEmpty get() = _items.value?.isEmpty() ?: true
    val items: LiveData<List<Emoji>>
        get() = _items
    val dataState: LiveData<DataState>
        get() = _dataState

    private val _dataState = MutableLiveData(DataState.NONE)
    private val _items = MutableLiveData<List<Emoji>>()

    fun emojis(modelScope: CoroutineScope,
               onFinish: (MutableLiveData<List<Emoji>>) -> Unit = {}) {

        if (isDataEmpty) {

            _dataState.value = DataState.START

            modelScope.launch(Dispatchers.IO) {

                val data = localDataSource.listEmojis()

                withContext(Dispatchers.Main) {
                    _items.value = data
                }

                if (isDataEmpty) {
                    val response = service.listEmojis()

                    if (response.isSuccessful) {

                        withContext(Dispatchers.Main) {
                            _items.value = response.body()

                            if (!isDataEmpty) {

                                withContext(Dispatchers.IO) {

                                    localDataSource.insertEmojis(_items.value!!)
                                }

                                onFinish.invoke(_items)
                                _dataState.value = DataState.LOADED
                            }

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onFinish.invoke(_items)
                        _dataState.value = DataState.LOADED
                    }
                }
            }
        } else {
            onFinish.invoke(_items)
        }
    }
}