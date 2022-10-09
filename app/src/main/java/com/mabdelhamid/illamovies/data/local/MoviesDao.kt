package com.mabdelhamid.illamovies.data.local

import androidx.room.*
import com.mabdelhamid.illamovies.data.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Data access object, an interface that offer all the operations that we need to do on the app local database.
 * every new operation needs to be listed here.
 * At compile time, Room automatically generates implementations of this DAO.
 */

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie): Long

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieById(id: Int): Int
}