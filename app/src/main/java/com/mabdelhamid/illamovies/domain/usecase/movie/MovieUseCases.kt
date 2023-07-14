package com.mabdelhamid.illamovies.domain.usecase.movie

import javax.inject.Inject

data class MovieUseCases @Inject constructor(
    val getAllMoviesUseCase: GetAllMoviesUseCase,
    val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    val addMovieToFavouritesUseCase: AddMovieToFavouritesUseCase,
    val deleteMovieFromFavouritesUseCase: DeleteMovieFromFavouritesUseCase
)
