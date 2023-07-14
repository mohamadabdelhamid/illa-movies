package com.mabdelhamid.illamovies.domain.usecase.movie

import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.model.ResponseWrapper
import com.mabdelhamid.illamovies.data.onError
import com.mabdelhamid.illamovies.data.onLoading
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    operator fun invoke(page: Int): Flow<DataState<ResponseWrapper<Movie>>> =
        flow {
            moviesRepository
                .getAllMovies(page = page)
                .collect { result ->
                    result
                        .onLoading { emit(DataState.Loading()) }
                        .onSuccess { emit(DataState.Success(data = it)) }
                        .onError { emit(DataState.Error(message = it)) }
                }
        }
}