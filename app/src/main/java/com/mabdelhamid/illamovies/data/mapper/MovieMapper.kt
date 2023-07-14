package com.mabdelhamid.illamovies.data.mapper

import com.mabdelhamid.illamovies.base.Mapper
import com.mabdelhamid.illamovies.data.model.MovieDto
import com.mabdelhamid.illamovies.domain.entity.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() : Mapper<MovieDto, Movie> {

    override fun map(input: MovieDto): Movie {
        with(input) {
            return Movie(
                id = this.id ?: 0,
                title = this.title ?: "",
                posterPath = this.posterPath ?: "",
                overview = this.overview ?: "",
                voteAverage = this.voteAverage ?: 0.0
            )
        }
    }
}