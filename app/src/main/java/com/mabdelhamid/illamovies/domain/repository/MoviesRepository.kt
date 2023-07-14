package com.mabdelhamid.illamovies.domain.repository

import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.model.BaseResponse
import com.mabdelhamid.illamovies.data.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getRemoteMovies(page: Int): Flow<DataState<BaseResponse<Movie>>>

    fun getFavouriteMovies(): Flow<List<Movie>>

    suspend fun addMovieToFavourites(movie: Movie): Flow<DataState<Long>>

    suspend fun deleteMovieFromFavourites(id: Int?): Flow<DataState<Int>>
}