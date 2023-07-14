package com.mabdelhamid.illamovies.ui.favourites

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import com.mabdelhamid.illamovies.domain.usecase.movie.MovieUseCases
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.*
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.FavouritesViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases
) : BaseViewModel<FavouritesViewEvent, FavouritesViewState, FavouritesViewEffect>() {

    init {
        getFavouriteMovies()
    }

    override fun initState(): FavouritesViewState = FavouritesViewState()

    override fun handleEvent(event: FavouritesViewEvent) = when (event) {
        is UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event.movieId)
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            movieUseCases
                .getFavouriteMoviesUseCase()
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

    private fun onUnFavouriteMovieClicked(movieId: Int) {
        viewModelScope.launch {
            movieUseCases
                .deleteMovieFromFavouritesUseCase(movieId = movieId)
                .collect()
        }
    }
}