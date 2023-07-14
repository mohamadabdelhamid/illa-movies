package com.mabdelhamid.illamovies.data

/**
 * Sealed class that will wraps the data and provide functions for each data state.
 *
 * @param T type of provided data
 * @property data actual data in case of success
 * @property message error message in case of failure
 */

sealed class DataState<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : DataState<T>(data = data)

    class Error<T>(message: String? = null) : DataState<T>(message = message)

    class Loading<T> : DataState<T>()
}

inline fun <T : Any> DataState<T>.onSuccess(action: (T?) -> Unit): DataState<T> {
    if (this is DataState.Success) action(data)
    return this
}

inline fun <T : Any> DataState<T>.onError(action: (String?) -> Unit): DataState<T> {
    if (this is DataState.Error) action(message)
    return this
}

inline fun <T : Any> DataState<T>.onLoading(action: () -> Unit): DataState<T> {
    if (this is DataState.Loading) action()
    return this
}