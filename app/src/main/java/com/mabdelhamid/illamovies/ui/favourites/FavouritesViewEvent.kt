package com.mabdelhamid.illamovies.ui.favourites

import com.mabdelhamid.illamovies.data.model.Movie

/**
 * Represents all the user actions happened in [FavouritesFragment] and needs to handled by [FavouritesViewModel]
 */

sealed class FavouritesViewEvent {

    data class UnFavouriteMovieClicked(
        val movie: Movie
    ) : FavouritesViewEvent()

}
