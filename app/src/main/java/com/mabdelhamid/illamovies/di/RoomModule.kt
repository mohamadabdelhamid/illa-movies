package com.mabdelhamid.illamovies.di

import android.content.Context
import androidx.room.Room
import com.mabdelhamid.illamovies.data.local.MoviesDao
import com.mabdelhamid.illamovies.data.local.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Contains the functions that responsible of injecting the app local database
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase =
        Room
            .databaseBuilder(
                context,
                MoviesDatabase::class.java,
                "movies-database"
            )
            .build()

    @Provides
    @Singleton
    fun provideMoviesDao(database: MoviesDatabase): MoviesDao = database.moviesDao()
}