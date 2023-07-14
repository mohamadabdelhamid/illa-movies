package com.mabdelhamid.illamovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mabdelhamid.illamovies.R
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.databinding.ItemMovieBinding
import com.mabdelhamid.illamovies.domain.entity.Movie

/**
 * Class to adapt the movies list to be displayed in a recyclerview.
 *
 * @property onFavouriteClicked function to be called with user toggle favourite item
 * @property onUnFavouriteClicked function to be called with user toggle unFavourite item
 */

class MoviesAdapter(
    private val onFavouriteClicked: ((Movie) -> Unit)? = null,
    private val onUnFavouriteClicked: ((Movie) -> Unit)? = null
) : ListAdapter<Movie, MoviesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            displayPoster(item)
            displayTitle(item)
            displayOverview(item)
            displayRating(item)
            displayFavouriteIcon(item)
        }

        private fun displayPoster(item: Movie) = with(binding.ivImage) {
            Glide
                .with(this)
                .load(item.posterUrl)
                .into(this)
        }

        private fun displayTitle(item: Movie) = with(binding.tvTitle) {
            text = item.title
        }

        private fun displayOverview(item: Movie) = with(binding.tvOverview) {
            text = item.overview
        }

        private fun displayRating(item: Movie) = with(binding.tvRating) {
            text = item.voteAverage.toString()
        }

        private fun displayFavouriteIcon(item: Movie) = with(binding.ivFavourite) {
            setImageResource(item.favouriteIconRes)
            setOnClickListener {
                if (!item.isFavourite) onFavouriteClicked?.invoke(item)
                else onUnFavouriteClicked?.invoke(item)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}