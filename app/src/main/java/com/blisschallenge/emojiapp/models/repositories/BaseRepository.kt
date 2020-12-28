package com.blisschallenge.emojiapp.models.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blisschallenge.emojiapp.helpers.RequestInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository {

    val dataState: LiveData<RequestInfo<Any>>
        get() = _dataState

    protected val _dataState = MutableLiveData(RequestInfo<Any>())

    fun <T> cacheOrRemoteRequest(
        modelScope: CoroutineScope,
        remoteRequest: (suspend () -> Response<T>)? = null,
        shouldRemoteRequest: suspend () -> Boolean = { true },
        dbRequest: suspend () -> T,
        dbCacheSave: suspend (T) -> Unit = {},
        onFinish: (MutableLiveData<RequestInfo<T>>) -> Unit = {}
    ) {

        _dataState.value = RequestInfo.start(null)

        modelScope.launch(Dispatchers.IO) {

            val data: T? = dbRequest.invoke()

            withContext(Dispatchers.Main) {
                _dataState.value = RequestInfo.start(data)
            }

            if (dataState.value?.isDataEmpty == true) {

                if (remoteRequest == null || !shouldRemoteRequest.invoke()) {

                    withContext(Dispatchers.Main) {

                        _dataState.value = RequestInfo.loaded(_dataState.value?.data)
                        onFinish.invoke(_dataState as MutableLiveData<RequestInfo<T>>)
                    }
                    return@launch
                }

                val response = remoteRequest.invoke()

                if (response.isSuccessful) {

                    withContext(Dispatchers.Main) {

                        _dataState.value = RequestInfo.loaded(response.body())

                        if (dataState.value?.isDataEmpty == false) {

                            withContext(Dispatchers.IO) {
                                dbCacheSave.invoke(dataState.value?.data as T)
                            }

                            _dataState.value = RequestInfo.loaded(response.body())
                            onFinish.invoke(_dataState as MutableLiveData<RequestInfo<T>>)
                        }
                    }
                } else {
                    _dataState.value = response.errorBody()?.string()?.let { RequestInfo.error(message = it, response.body()) }
                }
            } else {
                withContext(Dispatchers.Main) {

                    //TODO: Reuse this previous state better
                    if (dataState.value?.state != RequestInfo.DataState.LOADED) {

                        _dataState.value = RequestInfo.loaded(_dataState.value?.data)
                    }

                    onFinish.invoke(_dataState as MutableLiveData<RequestInfo<T>>)
                }
            }
        }
    }
}