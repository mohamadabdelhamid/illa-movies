package com.mabdelhamid.illamovies.repository

import android.content.Context
import com.mabdelhamid.illamovies.base.BaseRepository
import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.local.MoviesDao
import com.mabdelhamid.illamovies.data.model.BaseResponse
import com.mabdelhamid.illamovies.data.model.Movie
import com.mabdelhamid.illamovies.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * On point of access to the movies datasource (remote service or local database)
 *
 * @property apiService an instance of [ApiService] to get access to the remote service
 * @property moviesDao an instance [MoviesDao] to get access to the local database
 */

class MoviesRepository
constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val moviesDao: MoviesDao,
) : BaseRepository(context) {

    suspend fun getRemoteMovies(page: Int): Flow<DataState<BaseResponse<Movie>>> =
        flow {
            emit(DataState.Loading())
            emit(DataState.Success(apiService.getAllMovies(page = page)))
        }.catch { e ->
            emit(getApiFailureMessage(e))
        }

    fun getFavouriteMovies() = moviesDao.getMovies()

    suspend fun addMovieToFavourites(movie: Movie): Flow<DataState<Long>> =
        flow {
            emit(DataState.Loading())
            emit(DataState.Success(moviesDao.addMovie(movie)))
        }.catch { e ->
            emit(DataState.Error(message = e.message))
        }

    suspend fun deleteMovieFromFavourites(id: Int?): Flow<DataState<Int>> =
        flow {
            emit(DataState.Loading())
            emit(DataState.Success(moviesDao.deleteMovieById(id = id ?: -1)))
        }.catch { e ->
            emit(DataState.Error(message = e.message))
        }
}