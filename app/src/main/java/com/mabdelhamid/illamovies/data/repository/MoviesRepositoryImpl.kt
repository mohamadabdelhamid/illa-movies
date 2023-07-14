package com.mabdelhamid.illamovies.data.repository

import android.content.Context
import com.mabdelhamid.illamovies.base.BaseRepository
import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.local.MoviesDao
import com.mabdelhamid.illamovies.data.mapper.MovieMapper
import com.mabdelhamid.illamovies.data.model.ResponseWrapper
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.data.remote.ApiService
import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * One point of access to the movies datasource (remote service or local database)
 *
 * @property apiService an instance of [ApiService] to get access to the remote service
 * @property moviesDao an instance [MoviesDao] to get access to the local database
 */

class MoviesRepositoryImpl @Inject constructor(
    context: Context,
    private val apiService: ApiService,
    private val moviesDao: MoviesDao,
    private val movieMapper: MovieMapper
) : BaseRepository(context), MoviesRepository {

    override suspend fun getRemoteMovies(page: Int): Flow<DataState<ResponseWrapper<Movie>>> =
        flow {
            emit(DataState.Loading())
            val response = apiService.getAllMovies(page = page)
            val mappedResponse = ResponseWrapper(
                page = response.page,
                results = movieMapper.mapList(response.results ?: emptyList()),
                totalResults = response.totalResults,
                totalPages = response.totalPages
            )
            emit(DataState.Success(data = mappedResponse))

        }.catch { e ->
            emit(getApiFailureMessage(e))
        }

    override fun getFavouriteMovies(): Flow<List<Movie>> = moviesDao.getMovies()

    override suspend fun addMovieToFavourites(movie: Movie): Flow<DataState<Long>> =
        flow {
            emit(DataState.Loading())
            emit(DataState.Success(moviesDao.addMovie(movie)))
        }.catch { e ->
            emit(DataState.Error(message = e.message))
        }

    override suspend fun deleteMovieFromFavourites(id: Int?): Flow<DataState<Int>> =
        flow {
            emit(DataState.Loading())
            emit(DataState.Success(moviesDao.deleteMovieById(id = id ?: -1)))
        }.catch { e ->
            emit(DataState.Error(message = e.message))
        }
}