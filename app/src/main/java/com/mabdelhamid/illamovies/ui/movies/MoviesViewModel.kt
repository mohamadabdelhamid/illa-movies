package com.mabdelhamid.illamovies.ui.movies

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.data.model.Movie
import com.mabdelhamid.illamovies.data.onError
import com.mabdelhamid.illamovies.data.onLoading
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the business logic, UI state of [MoviesFragment], and consumes the events came from that screen.
 *
 * @property moviesRepository instance of [MoviesRepository] to interact with the datasource.
 */

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<MoviesViewState, MoviesViewEffect, MoviesViewEvent>() {

    private var currentMovies = mutableListOf<Movie>()
    private var currentPage = 1
    private var totalResults = 0
    private var canPaginate = false

    init {
        getMovies()
    }

    override fun initViewState(): MoviesViewState = MoviesViewState()

    override fun processEvent(event: MoviesViewEvent) = when (event) {
        is MoviesViewEvent.GetMovies -> getMovies()
        is MoviesViewEvent.GetMoreMovies -> getMoreMovies()
        is MoviesViewEvent.FavouriteMovieClicked -> onFavouriteMovieClicked(event)
        is MoviesViewEvent.UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event)
    }

    private fun getMovies() {
        currentPage = 1
        viewModelScope.launch {
            moviesRepository
                .getRemoteMovies(page = currentPage)
                .collect { result ->
                    result
                        .onLoading {
                            updateViewState(currentViewState.copy(isLoading = true))
                        }
                        .onSuccess {
                            val results = it?.results
                            totalResults = it?.totalPages ?: 0
                            currentMovies = results?.toMutableList() ?: mutableListOf()
                            checkIfCanPaginateAgain()
                            getFavouriteMovies()
                            updateViewState(currentViewState.copy(isLoading = false))
                        }
                        .onError {
                            updateViewState(
                                currentViewState.copy(
                                    isLoading = false,
                                    isEmptyState = currentMovies.isEmpty()
                                )
                            )
                            updateViewEffect(MoviesViewEffect.ShowError(message = it))
                        }
                }
        }
    }

    private fun getMoreMovies() {
        viewModelScope.launch {
            moviesRepository
                .getRemoteMovies(page = currentPage)
                .collect { result ->
                    result
                        .onLoading {
                            updateViewState(currentViewState.copy(isLoadingMore = true))
                        }
                        .onSuccess {
                            currentMovies.addAll(it?.results ?: emptyList())
                            checkIfCanPaginateAgain()
                            getFavouriteMovies()
                        }
                        .onError {
                            updateViewState(currentViewState.copy(isLoadingMore = false))
                            updateViewEffect(MoviesViewEffect.ShowError(message = it))
                        }
                }
        }
    }

    private fun checkIfCanPaginateAgain() {
        canPaginate = if (currentMovies.size < totalResults) {
            currentPage++
            true
        } else false
    }

    private fun onFavouriteMovieClicked(event: MoviesViewEvent.FavouriteMovieClicked) {
        viewModelScope.launch {
            moviesRepository
                .addMovieToFavourites(movie = event.movie.apply { isFavourite = true })
                .collect()
        }
    }

    private fun onUnFavouriteMovieClicked(event: MoviesViewEvent.UnFavouriteMovieClicked) {
        viewModelScope.launch {
            moviesRepository
                .deleteMovieFromFavourites(id = event.movie.id)
                .collect()
        }
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepository
                .getFavouriteMovies()
                .collect { favouriteMovies ->
                    if (favouriteMovies.isEmpty()) {
                        currentMovies.forEach { remoteMovie -> remoteMovie.isFavourite = false }
                    } else {
                        currentMovies.forEach { remoteMovie ->
                            remoteMovie.isFavourite =
                                favouriteMovies.find { it.id == remoteMovie.id } != null
                        }
                    }
                    updateViewState(
                        currentViewState.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            movies = currentMovies,
                            isEmptyState = currentMovies.isEmpty()
                        )
                    )
                }
        }
    }
}
