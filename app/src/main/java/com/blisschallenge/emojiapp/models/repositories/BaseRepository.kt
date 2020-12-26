package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.helpers.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository<T> {

    val isDataEmpty: Boolean
        get() = when(items.value) {
            is List<*> -> (items.value as List<*>).isEmpty()
            else -> items.value == null
        }

    val items: LiveData<T>
        get() = _items
    val dataState: LiveData<DataState>
        get() = _dataState

    protected val _dataState = MutableLiveData(DataState.NONE)
    protected val _items = MutableLiveData<T>()

    fun cacheOrRemoteRequest(modelScope: CoroutineScope,
                             remoteRequest: suspend () -> Response<T>,
                             dbRequest: suspend () -> T,
                             dbCacheSave: suspend (T) -> Unit,
                             onFinish: (MutableLiveData<T>) -> Unit = {}) {

        _dataState.value = DataState.START

        modelScope.launch(Dispatchers.IO) {

            val data = dbRequest.invoke()

            withContext(Dispatchers.Main) {
                _items.value = data
            }

            if (isDataEmpty) {
                val response = remoteRequest.invoke()

                if (response.isSuccessful) {

                    withContext(Dispatchers.Main) {
                        _items.value = response.body()

                        if (!isDataEmpty) {

                            withContext(Dispatchers.IO) {
                                dbCacheSave.invoke(_items.value!!)
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
    }
}