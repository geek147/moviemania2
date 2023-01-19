package com.envious.domain.repository

import com.envious.domain.model.Genre
import com.envious.domain.model.Movie
import com.envious.domain.model.MovieVideo
import com.envious.domain.model.UserReview
import com.envious.domain.util.Result

interface MovieRepository {

    suspend fun getPopular(
        page: Int
    ): Result<List<Movie>>

    suspend fun getTopRated(
        page: Int
    ): Result<List<Movie>>

    suspend fun getFavoriteMovies(): Result<List<Movie>>

    suspend fun insertFavoriteMovie(movie: Movie): Result<List<Movie>>

    suspend fun deleteFavoriteMovie(id: Int): Result<List<Movie>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieByGenre(
        page: Int,
        withGenre: Int
    ): Result<List<Movie>>

    suspend fun getUserReview(
        page: Int,
        movieId: Int
    ): Result<List<UserReview>>

    suspend fun getMovieVideo(
        movieId: Int
    ) : Result<List<MovieVideo>>
}


