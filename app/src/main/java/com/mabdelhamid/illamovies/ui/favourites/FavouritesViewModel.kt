package com.mabdelhamid.illamovies.ui.favourites

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the business logic, UI state of [FavouritesFragment], and consumes the events came from that screen.
 *
 * @property moviesRepository instance of [MoviesRepository] to interact with the datasource.
 */

@HiltViewModel
class FavouritesViewModel
@Inject
constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<FavouritesViewState, Any, FavouritesViewEvent>() {

    init {
        getFavouriteMovies()
    }

    override fun initViewState(): FavouritesViewState = FavouritesViewState()

    override fun processEvent(event: FavouritesViewEvent) = when (event) {
        is FavouritesViewEvent.UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event)
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepository
                .getFavouriteMovies()
                .collect { movies ->
                    updateViewState(
                        currentViewState.copy(
                            movies = movies,
                            isEmptyState = movies.isEmpty()
                        )
                    )
                }
        }
    }

    private fun onUnFavouriteMovieClicked(event: FavouritesViewEvent.UnFavouriteMovieClicked) {
        viewModelScope.launch {
            moviesRepository.deleteMovieFromFavourites(id = event.movie.id).collect()
        }
    }
}