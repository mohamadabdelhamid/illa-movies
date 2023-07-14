package com.mabdelhamid.illamovies.data.remote

import com.mabdelhamid.illamovies.BuildConfig
import com.mabdelhamid.illamovies.data.model.ResponseWrapper
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.data.remote.Url.All_MOVIES
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Represents all the operations that will be use for interacting with the remote server using
 * Retrofit.
 * every new API call needs to be listed here.
 */

interface ApiService {

    @GET(All_MOVIES)
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): ResponseWrapper<MovieDto>
}