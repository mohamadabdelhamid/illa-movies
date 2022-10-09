package com.mabdelhamid.illamovies.ui.movies

import com.mabdelhamid.illamovies.data.model.Movie

/**
 * Represents all the user actions happened in [MoviesFragment] and needs to handled by [MoviesViewModel]
 */

sealed class MoviesViewEvent {

    object GetMovies : MoviesViewEvent()

    object GetMoreMovies : MoviesViewEvent()

    data class FavouriteMovieClicked(
        val movie: Movie
    ) : MoviesViewEvent()

    data class UnFavouriteMovieClicked(
        val movie: Movie
    ) : MoviesViewEvent()

}
