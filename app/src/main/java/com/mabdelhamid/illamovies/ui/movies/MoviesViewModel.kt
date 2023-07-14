package com.mabdelhamid.illamovies.ui.movies

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.common.UiAlert
import com.mabdelhamid.illamovies.common.UiAlert.Type.ERROR
import com.mabdelhamid.illamovies.common.UiText
import com.mabdelhamid.illamovies.data.onError
import com.mabdelhamid.illamovies.data.onLoading
import com.mabdelhamid.illamovies.data.onSuccess
import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.domain.usecase.movie.MovieUseCases
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEffect
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.FavouriteMovieClicked
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.GetMoreMovies
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.GetMovies
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.UnFavouriteMovieClicked
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases
) : BaseViewModel<MoviesViewEvent, MoviesViewState, MoviesViewEffect>() {

    private var currentPage = 1
    private var totalCount = 0
    private var canPaginate = false

    override fun initState(): MoviesViewState = MoviesViewState()

    override fun handleEvent(event: MoviesViewEvent) = when (event) {
        is GetMovies -> getMovies()
        is GetMoreMovies -> getMoreMovies()
        is FavouriteMovieClicked -> onFavouriteMovieClicked(event.movie)
        is UnFavouriteMovieClicked -> onUnFavouriteMovieClicked(event.movieId)
    }

    init {
        getMovies()
    }

    private fun getMovies() {
        currentPage = 1
        viewModelScope.launch {
            movieUseCases
                .getAllMoviesUseCase(page = currentPage)
                .collect { result ->
                    result
                        .onLoading {
                            setState { copy(isLoading = true) }
                        }
                        .onSuccess {
                            totalCount = it?.totalResults ?: 0
                            setState {
                                copy(
                                    isLoading = false,
                                    movies = it?.results ?: emptyList()
                                )
                            }
                            checkIfCanPaginate()
                        }
                        .onError {
                            setState { copy(isLoading = false) }
                            setAlert { UiAlert(type = ERROR, text = UiText.String(it)) }
                        }
                }
        }
    }

    private fun getMoreMovies() {
        viewModelScope.launch {
            movieUseCases
                .getAllMoviesUseCase(page = currentPage)
                .collect { result ->
                    result
                        .onLoading {
                            setState { copy(isLoadingMore = true) }
                        }
                        .onSuccess {
                            setState {
                                copy(
                                    isLoadingMore = false,
                                    movies = movies + (it?.results ?: emptyList())
                                )
                            }
                            checkIfCanPaginate()
                        }
                        .onError {
                            setState { copy(isLoadingMore = false) }
                            setAlert { UiAlert(type = ERROR, text = UiText.String(it)) }
                        }
                }
        }
    }

    private fun checkIfCanPaginate() {
        canPaginate = if (state.movies.size < totalCount) {
            currentPage++
            true
        } else false
    }

    private fun onFavouriteMovieClicked(movie: Movie) {
        viewModelScope.launch {
            movieUseCases
                .addMovieToFavouritesUseCase(movie = movie)
                .collect()
        }
    }

    private fun onUnFavouriteMovieClicked(movieId: Int) {
        viewModelScope.launch {
            movieUseCases
                .deleteMovieFromFavouritesUseCase(movieId = movieId)
                .collect()
        }
    }

//    private fun getFavouriteMovies() {
//        viewModelScope.launch {
//            moviesRepository
//                .getFavouriteMovies()
//                .collect { favouriteMovies ->
////                    if (favouriteMovies.isEmpty()) {
////                        currentMovies.forEach { remoteMovie -> remoteMovie.isFavourite = false }
////                    } else {
////                        currentMovies.forEach { remoteMovie ->
////                            remoteMovie.isFavourite =
////                                favouriteMovies.find { it.id == remoteMovie.id } != null
////                        }
////                    }
//                    setState {
//                        copy(
//                            isLoading = false,
//                            isLoadingMore = false,
//                            movies = currentMovies,
//                            isEmptyState = currentMovies.isEmpty()
//                        )
//                    }
//                }
//        }
//    }
}
