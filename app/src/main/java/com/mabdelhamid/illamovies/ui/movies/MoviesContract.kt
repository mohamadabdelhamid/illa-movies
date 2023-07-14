package com.mabdelhamid.illamovies.ui.movies

import com.mabdelhamid.illamovies.base.ViewEffect
import com.mabdelhamid.illamovies.base.ViewEvent
import com.mabdelhamid.illamovies.base.ViewState
import com.mabdelhamid.illamovies.domain.entity.Movie

class MoviesContract {

    sealed class MoviesViewEvent : ViewEvent {

        object GetMovies : MoviesViewEvent()

        object GetMoreMovies : MoviesViewEvent()

        data class FavouriteMovieClicked(val movie: Movie) : MoviesViewEvent()

        data class UnFavouriteMovieClicked(val movie: Movie) : MoviesViewEvent()
    }

    data class MoviesViewState(
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val isEmptyState: Boolean = false
    ): ViewState

    sealed class MoviesViewEffect : ViewEffect
}