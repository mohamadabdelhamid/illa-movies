package com.mabdelhamid.illamovies.ui.activity.main

/**
 * holds the UI state of [MainActivity]
 */

data class MainViewState(
    val favouritesCount: Int = 0,
    val isEmptyFavourites: Boolean = true
)
