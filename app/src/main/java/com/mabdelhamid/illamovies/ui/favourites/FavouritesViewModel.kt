package com.mabdelhamid.illamovies.ui.favourites

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.repository.MoviesRepository
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.*
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.FavouritesViewEvent.*
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
class FavouritesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<FavouritesViewEvent, FavouritesViewState, FavouritesViewEffect>() {

    init {
        getFavouriteMovies()
    }

    override fun initState(): FavouritesViewState = FavouritesViewState()

    override fun handleEvent(event: FavouritesViewEvent) = when (event) {
        is UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event)
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepository
                .getFavouriteMovies()
                .collect {
                    setState {
                        copy(
                            movies = it,
                            isEmptyState = it.isEmpty()
                        )
                    }
                }
        }
    }

    private fun onUnFavouriteMovieClicked(event: UnFavouriteMovieClicked) {
        viewModelScope.launch {
            moviesRepository.deleteMovieFromFavourites(id = event.movie.id).collect()
        }
    }
}