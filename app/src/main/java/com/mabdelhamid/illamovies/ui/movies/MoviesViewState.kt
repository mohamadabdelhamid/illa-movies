package com.mabdelhamid.illamovies.ui.movies

import com.mabdelhamid.illamovies.data.model.Movie

/**
 * holds the UI state of [MoviesFragment]
 */

data class MoviesViewState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val isEmptyState: Boolean = false
)
