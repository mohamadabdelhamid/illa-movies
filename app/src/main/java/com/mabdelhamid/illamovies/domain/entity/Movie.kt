package com.mabdelhamid.illamovies.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: Double,
    val isFavourite: Boolean = false
)