package com.mabdelhamid.illamovies.ui.favourites

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mabdelhamid.illamovies.base.BaseFragment
import com.mabdelhamid.illamovies.data.model.Movie
import com.mabdelhamid.illamovies.databinding.FragmentFavouritesBinding
import com.mabdelhamid.illamovies.ui.adapter.MoviesAdapter
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.*
import com.mabdelhamid.illamovies.ui.favourites.FavouritesContract.FavouritesViewEvent.*
import com.mabdelhamid.illamovies.util.extension.collectOnLifecycleStarted
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the favourite movies screen that displays the list of movies that user favourites.
 * UI state of this screen will be saved in [FavouritesViewModel] and all the user actions needs
 * to be passed to that class to be handled.
 */

@AndroidEntryPoint
class FavouritesFragment :
    BaseFragment<FragmentFavouritesBinding>(FragmentFavouritesBinding::inflate) {

    private val viewModel by viewModels<FavouritesViewModel>()
    private val moviesAdapter by lazy {
        MoviesAdapter(
            onUnFavouriteClicked = { onUnFavouriteClicked(it) }
        )
    }

    override fun initUi() {
        initMoviesRecycler()
    }

    override fun initObservers() {
        with(viewModel) {
            viewState.collectOnLifecycleStarted(viewLifecycleOwner) { state ->
                displayMovies(state)
                toggleEmptyState(state)
            }
        }
    }

    private fun initMoviesRecycler() = with(binding.rvMovies) {
        adapter = moviesAdapter
    }

    private fun onUnFavouriteClicked(movie: Movie) =
        viewModel.setEvent(UnFavouriteMovieClicked(movie = movie))

    private fun displayMovies(state: FavouritesViewState) =
        moviesAdapter.submitList(state.movies)

    private fun toggleEmptyState(state: FavouritesViewState) = with(binding.wEmptyState) {
        isVisible = state.isEmptyState
    }
}