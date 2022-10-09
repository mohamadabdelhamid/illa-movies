package com.mabdelhamid.illamovies.ui.activity.main

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.repository.MoviesRepository
import com.mabdelhamid.illamovies.ui.favourites.FavouritesViewEvent
import com.mabdelhamid.illamovies.ui.favourites.FavouritesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the business logic, UI state of [MainActivity], and consumes the events came from that screen.
 *
 * @property moviesRepository instance of [MoviesRepository] to interact with the datasource.
 */

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<MainViewState, Any, Any>() {

    init {
        getFavouriteMovies()
    }

    override fun initViewState(): MainViewState = MainViewState()

    override fun processEvent(event: Any) = Unit

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepository
                .getFavouriteMovies()
                .collect { movies ->
                    updateViewState(
                        currentViewState.copy(
                            favouritesCount = movies.size,
                            isEmptyFavourites = movies.isEmpty()
                        )
                    )
                }
        }
    }
}