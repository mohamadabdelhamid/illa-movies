package com.mabdelhamid.illamovies.di

import com.mabdelhamid.illamovies.data.repository.MoviesRepositoryImpl
import com.mabdelhamid.illamovies.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Contains the functions that responsible of injecting app repositories
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMoviesRepository(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}