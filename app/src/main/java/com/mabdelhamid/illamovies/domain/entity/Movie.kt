package com.mabdelhamid.illamovies.domain.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mabdelhamid.illamovies.R

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: Double,
    val isFavourite: Boolean = false
) {

    val posterUrl: String
        get() = "https://image.tmdb.org/t/p/w500/$posterPath"

    @get:DrawableRes
    val favouriteIconRes: Int
        get() = if (isFavourite) R.drawable.ic_favorite else R.drawable.ic_favorite_border

}