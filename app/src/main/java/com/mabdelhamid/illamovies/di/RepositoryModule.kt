package com.mabdelhamid.illamovies.di

import android.content.Context
import com.mabdelhamid.illamovies.data.local.MoviesDao
import com.mabdelhamid.illamovies.data.remote.ApiService
import com.mabdelhamid.illamovies.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Contains the functions that responsible of injecting app repositories
 */

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        moviesDao: MoviesDao
    ): MoviesRepository =
        MoviesRepository(
            context = context,
            apiService = apiService,
            moviesDao = moviesDao
        )
}