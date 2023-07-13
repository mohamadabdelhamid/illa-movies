package com.mabdelhamid.illamovies.ui.activity.main

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.repository.MoviesRepository
import com.mabdelhamid.illamovies.ui.activity.main.MainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the business logic, UI state of [MainActivity], and consumes the events came from that screen.
 *
 * @property moviesRepository instance of [MoviesRepository] to interact with the datasource.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel<MainViewEvent, MainViewState, MainViewEffect>() {

    override fun initState(): MainViewState = MainViewState()

    override fun handleEvent(event: MainViewEvent) = Unit

    init {
        getFavouriteMovies()
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepository
                .getFavouriteMovies()
                .collect {
                    setState {
                        copy(
                            favouritesCount = it.size,
                            isEmptyFavourites = it.isEmpty()
                        )
                    }
                }
        }
    }
}