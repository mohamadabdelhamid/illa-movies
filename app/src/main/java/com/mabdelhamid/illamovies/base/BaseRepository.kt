package com.mabdelhamid.illamovies.base

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.mabdelhamid.illamovies.R
import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.util.extension.isOffline
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * A base class for all repositories in the app.
 *
 * @property context application context to access string resources
 */

open class BaseRepository(
    private val context: Context
) {

    protected fun <T> getApiFailureMessage(e: Throwable): DataState.Error<T> {
        return if (context.isOffline()) {
            DataState.Error(message = context.getString(R.string.error_connect_to_internet))
        } else {
            when (e) {
                is SocketTimeoutException ->
                    DataState.Error(message = context.getString(R.string.error_bad_network_connection))

                is HttpException ->
                    DataState.Error(message = e.localizedMessage ?: "")

                is ConnectException, is UnknownHostException, is IOException ->
                    DataState.Error(message = context.getString(R.string.error_server_connect_error))

                is JsonSyntaxException ->
                    DataState.Error(message = context.getString(R.string.error_invalid_server_data))

                else ->
                    DataState.Error(message = context.getString(R.string.error_internal_server_error))
            }
        }
    }
}