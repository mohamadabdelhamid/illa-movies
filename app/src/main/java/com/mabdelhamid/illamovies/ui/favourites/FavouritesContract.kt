package com.mabdelhamid.illamovies.ui.favourites

import com.mabdelhamid.illamovies.base.ViewEffect
import com.mabdelhamid.illamovies.base.ViewEvent
import com.mabdelhamid.illamovies.base.ViewState
import com.mabdelhamid.illamovies.data.model.Movie

class FavouritesContract {

    sealed class FavouritesViewEvent : ViewEvent {
        data class UnFavouriteMovieClicked(
            val movie: Movie
        ) : FavouritesViewEvent()

    }

    data class FavouritesViewState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val isEmptyState: Boolean = false
    ) : ViewState

    sealed class FavouritesViewEffect : ViewEffect
}