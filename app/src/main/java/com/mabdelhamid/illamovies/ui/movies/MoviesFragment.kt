package com.mabdelhamid.illamovies.ui.movies

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mabdelhamid.illamovies.base.BaseFragment
import com.mabdelhamid.illamovies.databinding.FragmentMoviesBinding
import com.mabdelhamid.illamovies.ui.adapter.MoviesAdapter
import com.mabdelhamid.illamovies.util.PaginationScrollListener
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
            onFavouriteClicked = {
                viewModel.processEvent(MoviesViewEvent.FavouriteMovieClicked(movie = it))
            },
            onUnFavouriteClicked = {
                viewModel.processEvent(MoviesViewEvent.UnFavouriteMovieClicked(movie = it))
            }
        )
    }

    override fun initUi() {
        with(binding) {
            swipeRefresh.setOnRefreshListener { getMovies() }
            with(rvMovies) {
                adapter = moviesAdapter
                val layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                    override fun isLoading(): Boolean =
                        viewModel.viewState.value?.isLoadingMore == true

                    override fun onLoadMore() =
                        viewModel.processEvent(MoviesViewEvent.GetMoreMovies)
                })
                this.layoutManager = layoutManager
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        with(viewModel) {
            viewState.observe(viewLifecycleOwner) { state ->
                with(binding) {
                    if (state.isLoading) swipeRefresh.isRefreshing = true
                    else if (swipeRefresh.isRefreshing) swipeRefresh.isRefreshing = false
                    linearProgressbar.isVisible = state.isLoadingMore
                    wEmptyState.isVisible = state.isEmptyState
                    moviesAdapter.apply {
                        submitList(state.movies)
                        notifyDataSetChanged()
                    }
                }
            }

            viewModel.viewEffect.observe(viewLifecycleOwner) { effect ->
                when (effect) {
                    is MoviesViewEffect.ShowError -> showError(effect.message)
                }
            }
        }
    }

    private fun getMovies() = viewModel.processEvent(MoviesViewEvent.GetMovies)

    private fun showError(message: String?) = Toast.makeText(
        requireContext(),
        message,
        Toast.LENGTH_SHORT
    ).show()
}