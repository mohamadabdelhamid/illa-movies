package com.mabdelhamid.illamovies.ui.movies

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.common.UiAlert
import com.mabdelhamid.illamovies.common.UiAlert.Type.ERROR
import com.mabdelhamid.illamovies.common.UiText
import com.mabdelhamid.illamovies.data.model.Movie
import com.mabdelhamid.illamovies.data.onError
import com.mabdelhamid.illamovies.data.onLoading
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.data.repository.MoviesRepositoryImpl
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.*
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.*
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
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<MoviesViewEvent, MoviesViewState, MoviesViewEffect>() {

    private var currentMovies = mutableListOf<Movie>()
    private var currentPage = 1
    private var totalResults = 0
    private var canPaginate = false

    override fun initState(): MoviesViewState = MoviesViewState()

    override fun handleEvent(event: MoviesViewEvent) = when (event) {
        is GetMovies -> getMovies()
        is GetMoreMovies -> getMoreMovies()
        is FavouriteMovieClicked -> onFavouriteMovieClicked(event)
        is UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event)
    }

    init {
        getMovies()
    }

    private fun getMovies() {
        currentPage = 1
        viewModelScope.launch {
            moviesRepository
                .getRemoteMovies(page = currentPage)
                .collect { result ->
                    result
                        .onLoading {
                            setState { copy(isLoading = true) }
                        }
                        .onSuccess {
                            val results = it?.results
                            totalResults = it?.totalPages ?: 0
                            currentMovies = results?.toMutableList() ?: mutableListOf()
                            checkIfCanPaginateAgain()
                            getFavouriteMovies()
                            setState { copy(isLoading = false) }
                        }
                        .onError {
                            setState {
                                copy(
                                    isLoading = false,
                                    isEmptyState = currentMovies.isEmpty()
                                )
                            }
                            setAlert { UiAlert(type = ERROR, text = UiText.String(it)) }
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
                            setState { copy(isLoadingMore = true) }
                        }
                        .onSuccess {
                            currentMovies.addAll(it?.results ?: emptyList())
                            checkIfCanPaginateAgain()
                            getFavouriteMovies()
                        }
                        .onError {
                            setState { copy(isLoadingMore = false) }
                            setAlert { UiAlert(type = ERROR, text = UiText.String(it)) }
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

    private fun onFavouriteMovieClicked(event: FavouriteMovieClicked) {
        viewModelScope.launch {
            moviesRepository
                .addMovieToFavourites(movie = event.movie.apply { isFavourite = true })
                .collect()
        }
    }

    private fun onUnFavouriteMovieClicked(event: UnFavouriteMovieClicked) {
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
                    setState {
                        copy(
                            isLoading = false,
                            isLoadingMore = false,
                            movies = currentMovies,
                            isEmptyState = currentMovies.isEmpty()
                        )
                    }
                }
        }
    }
}
