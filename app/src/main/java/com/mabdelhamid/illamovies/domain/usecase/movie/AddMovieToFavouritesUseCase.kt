package com.mabdelhamid.illamovies.domain.usecase.movie

import com.mabdelhamid.illamovies.data.DataState
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddMovieToFavouritesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {

    operator fun invoke(movie: Movie) =
        flow {
            val updatedMovie = movie.copy(isFavourite = true)
            moviesRepository
                .addMovieToFavourites(movie = updatedMovie)
                .collect { result ->
                    result
                        .onSuccess { emit(DataState.Success(data = it)) }
                }

        }
}