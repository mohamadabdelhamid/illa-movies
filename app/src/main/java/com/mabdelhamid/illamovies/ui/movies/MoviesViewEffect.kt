package com.mabdelhamid.illamovies.ui.movies

/**
 * Represents all the side effect needs to handled just once by [MoviesFragment]
 */

sealed class MoviesViewEffect {

    data class ShowError(
        val message: String?
    ) : MoviesViewEffect()

}
