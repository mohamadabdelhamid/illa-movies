package com.mabdelhamid.illamovies.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that wraps the results came from the API service.
 *
 * @param T type of the data class that represents the single item of the list
 * @property page the page number of the results list
 * @property results the actual list of items
 * @property totalResults total count of items that are available in the remote server
 * @property totalPages total count of pages of items that are available in the remote server
 */

data class BaseResponse<T>(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") var results: List<T>? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
    @SerializedName("total_pages") val totalPages: Int? = null
)