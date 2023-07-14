package com.mabdelhamid.illamovies.domain.usecase.movie

import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavouriteMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    operator fun invoke(): Flow<List<Movie>> =
        flow {
            moviesRepository
                .getFavouriteMovies()
                .collect { result ->
                    emit(result)
                }
        }
}