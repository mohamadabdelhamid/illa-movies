package com.mabdelhamid.illamovies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Data class that represents the movie item came from remote server, also represents the movie item
 * to be stored in the local database
 */

@Entity(tableName = "movies")
data class Movie(
    var isFavourite: Boolean = false,
    @SerializedName("id") @PrimaryKey val id: Int? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
)