package com.envious.data.repository

import android.util.Log
import com.envious.data.BuildConfig
import com.envious.data.local.dao.FavoriteDao
import com.envious.data.local.model.FavoriteEntity
import com.envious.data.mapper.*
import com.envious.data.remote.MovieApiService
import com.envious.data.util.Constants
import com.envious.domain.model.Genre
import com.envious.domain.model.Movie
import com.envious.domain.model.MovieVideo
import com.envious.domain.model.UserReview
import com.envious.domain.repository.MovieRepository
import com.envious.domain.util.Result
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val favoriteDao: FavoriteDao
) : MovieRepository {

    override suspend fun getPopular(
        page: Int,
    ): Result<List<Movie>> {
        return try {
            val result = movieApiService.getPopularMovie(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page
            )
            if (result.isSuccessful) {
                val remoteMapper = MovieItemRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.movieItems
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<Movie>()
                    items.forEach {
                        val data = remoteMapper.transform(it!!)
                        data.isPopularMovie = true
                        listData.add(data)
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getTopRated(
        page: Int,
    ): Result<List<Movie>> {
        return try {
            val result = movieApiService.getTopRatedMovie(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page
            )
            if (result.isSuccessful) {
                val remoteMapper = MovieItemRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.movieItems
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<Movie>()
                    items.forEach {
                        listData.add(remoteMapper.transform(it!!))
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getFavoriteMovies(): Result<List<Movie>> {
        val localMapper = MovieItemLocalMapper()
        val itemsFavorite = favoriteDao.getAllFavorites()
        val listData = mutableListOf<Movie>()

        itemsFavorite.forEach {
            listData.add(localMapper.transform(it!!))
        }
        return Result.Success(listData.toList())
    }

    override suspend fun insertFavoriteMovie(movie: Movie): Result<List<Movie>> {
        favoriteDao.insert(
            FavoriteEntity(
                id = movie.id,
                adult = movie.adult,
                backdropPath = movie.backdropPath,
                originalLanguage = movie.originalLanguage,
                originalTitle = movie.originalTitle,
                overview = movie.overview,
                popularity = movie.popularity,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                title = movie.title,
                video = movie.video,
            )
        )

        return getFavoriteMovies()
    }

    override suspend fun deleteFavoriteMovie(id: Int): Result<List<Movie>> {
        favoriteDao.delete(id)
        return getFavoriteMovies()
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        return try {
            val result = movieApiService.getListGenre(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
            )
            if (result.isSuccessful) {
                val remoteMapper = GenreRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.genres
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<Genre>()
                    items.forEach {
                        val data = remoteMapper.transform(it!!)
                        listData.add(data)
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }

    }

    override suspend fun getMovieByGenre(page: Int, withGenre: Int): Result<List<Movie>> {
        return try {
            val result = movieApiService.getListMovieByGenre(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page,
                withGenre = withGenre
            )
            if (result.isSuccessful) {
                val remoteMapper = MovieItemRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.movieItems
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<Movie>()
                    items.forEach {
                        val data = remoteMapper.transform(it!!)
                        data.isPopularMovie = true
                        listData.add(data)
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getUserReview(page: Int, movieId: Int): Result<List<UserReview>> {
        return try {
            val result = movieApiService.getUserReview(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                page = page,
                movieId = movieId
            )
            if (result.isSuccessful) {
                val remoteMapper = UserReviewRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.results
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<UserReview>()
                    items.forEach {
                        val data = remoteMapper.transform(it!!)
                        listData.add(data)
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getMovieVideo(movieId: Int): Result<List<MovieVideo>> {
        return try {
            val result = movieApiService.getMovieVideo(
                apiKey = BuildConfig.API_KEY,
                language = Constants.MOVIE_LANGUAGE,
                movieId = movieId
            )
            if (result.isSuccessful) {
                val remoteMapper = MovieVideoRemoteMapper()
                val remoteData = result.body()
                val items = remoteData?.videos
                return if (remoteData != null && !items.isNullOrEmpty()) {
                    val listData = mutableListOf<MovieVideo>()
                    items.forEach {
                        val data = remoteMapper.transform(it!!)
                        listData.add(data)
                    }
                    Result.Success(listData.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                return Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }
}
