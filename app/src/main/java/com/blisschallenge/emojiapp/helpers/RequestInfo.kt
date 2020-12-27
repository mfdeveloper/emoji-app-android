package com.blisschallenge.emojiapp.helpers

data class RequestInfo<out T>(
    val state: DataState = DataState.NONE,
    val data: T? = null,
    var message: String? = ""
) {

    enum class DataState {
        NONE, START, LOADED, ERROR
    }

    val isDataEmpty: Boolean
        get() = when(data) {
            is List<*> -> (data as? Collection<*>).isNullOrEmpty()
            is String -> (data as? String).isNullOrBlank()
            else -> data == null
        }

    companion object {

        @JvmStatic
        fun <T> start(data: T? = null) = RequestInfo(DataState.START, data)
        @JvmStatic
        fun <T> loaded(data: T?) = RequestInfo(DataState.LOADED, data)
        @JvmStatic
        fun <T> error(message: String, data: T? = null) = RequestInfo(DataState.ERROR, data, message)
    }
}

