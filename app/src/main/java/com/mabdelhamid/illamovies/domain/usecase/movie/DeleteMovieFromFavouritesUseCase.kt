package com.mabdelhamid.illamovies.domain.usecase.movie

import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteMovieFromFavouritesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    operator fun invoke(movieId: Int) =
        flow {
            moviesRepository
                .deleteMovieFromFavourites(movieId = movieId)
                .collect { result ->
                    result
                        .onSuccess { emit(DataState.Success(data = it)) }
                }

        }
}