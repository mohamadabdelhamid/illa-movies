package com.mabdelhamid.illamovies.ui.activity.main

import androidx.lifecycle.viewModelScope
import com.mabdelhamid.illamovies.base.BaseViewModel
import com.mabdelhamid.illamovies.data.repository.MoviesRepositoryImpl
import com.mabdelhamid.illamovies.ui.activity.main.MainContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the business logic, UI state of [MainActivity], and consumes the events came from that screen.
 *
 * @property moviesRepositoryImpl instance of [MoviesRepositoryImpl] to interact with the datasource.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesRepositoryImpl: MoviesRepositoryImpl
) : BaseViewModel<MainViewEvent, MainViewState, MainViewEffect>() {

    override fun initState(): MainViewState = MainViewState()

    override fun handleEvent(event: MainViewEvent) = Unit

    init {
        getFavouriteMovies()
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            moviesRepositoryImpl
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