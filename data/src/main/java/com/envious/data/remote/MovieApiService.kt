package com.envious.data.remote

import com.envious.data.remote.response.GenreResponse
import com.envious.data.remote.response.MovieResponse
import com.envious.data.remote.response.UserReviewResponse
import com.envious.data.remote.response.VideoResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("3/movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("3/genre/movie/list")
    suspend fun getListGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<GenreResponse>

    @GET("3/discover/movie")
    suspend fun getListMovieByGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("with_genres") withGenre: Int
    ): Response<MovieResponse>

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getUserReview(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<UserReviewResponse>

    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<VideoResponse>

    companion object {
        operator fun invoke(retrofit: Retrofit): MovieApiService = retrofit.create(MovieApiService::class.java)
    }
}
