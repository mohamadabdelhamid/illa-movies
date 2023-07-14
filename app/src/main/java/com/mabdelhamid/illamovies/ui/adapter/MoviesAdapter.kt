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
 * @property onUnFavouriteClicked function to be called with user toggle unfavourite item
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

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            with(binding) {
                with(ivImage) {
                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w500/${item.posterPath}")
                        .into(this)
                }
                tvTitle.text = item.title
                tvOverview.text = item.overview
                tvRate.text = item.voteAverage.toString()
                with(ivFavourite) {
                    setImageResource(
                        if (item.isFavourite) R.drawable.ic_favorite
                        else R.drawable.ic_favorite_border
                    )
                    setOnClickListener {
                        if (!item.isFavourite) {
                            onFavouriteClicked?.invoke(item)
                        } else {
                            onUnFavouriteClicked?.invoke(item)
                        }
                    }
                }
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