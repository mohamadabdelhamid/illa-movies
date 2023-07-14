package com.mabdelhamid.illamovies.di

import com.mabdelhamid.illamovies.base.Mapper
import com.mabdelhamid.illamovies.data.mapper.MovieMapper
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.domain.entity.Movie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindMovieMapper(mapper: MovieMapper): Mapper<MovieDto, Movie>
}