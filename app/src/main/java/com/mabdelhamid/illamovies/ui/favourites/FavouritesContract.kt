package com.mabdelhamid.illamovies.ui.favourites

import com.mabdelhamid.illamovies.base.ViewEffect
import com.mabdelhamid.illamovies.base.ViewEvent
import com.mabdelhamid.illamovies.base.ViewState
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.domain.entity.Movie

class FavouritesContract {

    sealed class FavouritesViewEvent : ViewEvent {
        data class UnFavouriteMovieClicked(
            val movieId: Int
        ) : FavouritesViewEvent()

    }

    data class FavouritesViewState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val isEmptyState: Boolean = false
    ) : ViewState

    sealed class FavouritesViewEffect : ViewEffect
}