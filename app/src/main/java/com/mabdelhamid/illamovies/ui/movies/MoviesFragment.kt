package com.mabdelhamid.illamovies.ui.movies

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mabdelhamid.illamovies.base.BaseFragment
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.databinding.FragmentMoviesBinding
import com.mabdelhamid.illamovies.domain.entity.Movie
import com.mabdelhamid.illamovies.ui.adapter.MoviesAdapter
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.*
import com.mabdelhamid.illamovies.ui.movies.MoviesContract.MoviesViewEvent.*
import com.mabdelhamid.illamovies.util.PaginationScrollListener
import com.mabdelhamid.illamovies.util.extension.collectOnLifecycleStarted
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the movies screen that displays the list of all movies.
 * UI state of this screen will be saved in [MoviesViewModel] and all the user actions needs
 * to be passed to that class to be handled.
 */

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    private val viewModel by viewModels<MoviesViewModel>()
    private val moviesAdapter by lazy {
        MoviesAdapter(
            onFavouriteClicked = { onFavouriteClicked(it) },
            onUnFavouriteClicked = { onUnFavouriteClicked(it) }
        )
    }

    override fun initUi() {
        initSwipeRefresh()
        initMoviesRecycler()
    }

    override fun initObservers() {
        with(viewModel) {
            viewState.collectOnLifecycleStarted(viewLifecycleOwner) { state ->
                toggleLoading(state)
                displayMovies(state)
                toggleEmptyState(state)
            }

            viewAlert.collectOnLifecycleStarted(viewLifecycleOwner) { alert ->
                showAlert(alert)
            }
        }

    }

    private fun initSwipeRefresh() = with(binding.swipeRefresh) {
        setOnRefreshListener { getMovies() }
    }


    private fun initMoviesRecycler() = with(binding.rvMovies) {
        adapter = moviesAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLoading(): Boolean = viewModel.state.isLoadingMore

            override fun onLoadMore() =
                viewModel.setEvent(GetMoreMovies)
        })
        this.layoutManager = layoutManager
    }

    private fun onFavouriteClicked(movie: Movie) =
        viewModel.setEvent(FavouriteMovieClicked(movie = movie))

    private fun onUnFavouriteClicked(movie: Movie) =
        viewModel.setEvent(UnFavouriteMovieClicked(movie = movie))

    private fun getMovies() = viewModel.setEvent(GetMovies)

    private fun toggleLoading(state: MoviesViewState) = with(binding) {
        swipeRefresh.isRefreshing = state.isLoading
        linearProgressbar.isVisible = state.isLoadingMore
    }

    private fun displayMovies(state: MoviesViewState) = moviesAdapter.apply {
        submitList(state.movies)
        notifyDataSetChanged()
    }

    private fun toggleEmptyState(state: MoviesViewState) = with(binding) {
        wEmptyState.isVisible = state.isEmptyState
    }
}