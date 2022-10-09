package com.mabdelhamid.illamovies.ui.favourites

import com.mabdelhamid.illamovies.data.model.Movie

/**
 * holds the UI state of [FavouritesFragment]
 */

data class FavouritesViewState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val isEmptyState: Boolean = false,
)
