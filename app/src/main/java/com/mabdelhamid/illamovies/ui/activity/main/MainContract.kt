package com.mabdelhamid.illamovies.ui.activity.main

import com.mabdelhamid.illamovies.base.ViewEffect
import com.mabdelhamid.illamovies.base.ViewEvent
import com.mabdelhamid.illamovies.base.ViewState

class MainContract {

    sealed class MainViewEvent: ViewEvent

    data class MainViewState(
        val favouritesCount: Int = 0,
        val isEmptyFavourites: Boolean = true
    ): ViewState

    sealed class MainViewEffect: ViewEffect
}