package com.mabdelhamid.illamovies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mabdelhamid.illamovies.domain.entity.Movie

/**
 * Defines the database configuration and serves as the app's main access point to the persisted data.
 * For each DAO class that is associated with the database, the database class must define an
 * abstract method that has zero arguments and returns an instance of the DAO class.
 */

@Database(entities = [Movie::class], version = 2)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
}