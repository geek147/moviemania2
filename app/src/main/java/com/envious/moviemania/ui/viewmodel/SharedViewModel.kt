package com.envious.moviemania.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.envious.data.dispatchers.CoroutineDispatchers
import com.envious.data.usecase.* // ktlint-disable no-wildcard-imports
import com.envious.domain.model.Movie
import com.envious.domain.util.Result
import com.envious.moviemania.utils.Intent
import com.envious.moviemania.utils.State
import com.envious.moviemania.utils.ViewState
import com.envious.searchphoto.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getTopRatedUseCase: GetTopRatedUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val insertToFavoriteUseCase: InsertToFavoriteUseCase,
    private val deleteFromFavoriteUseCase: DeleteFromFavoriteUseCase,
    private val getGenreUseCase: GetGenreUseCase,
    private val getMovieByGenreUseCase: GetMovieByGenreUseCase,
    private val getUserReviewUseCase: GetUserReviewUseCase,
    private val getMovieVideoUseCase: GetMovieVideoUseCase,
    private val ioDispatchers: CoroutineDispatchers,
) : BaseViewModel<Intent, State>(State()) {

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            Intent.GetFavorites -> {
                getFavorites()
            }
            Intent.GetPopular -> getPopularFavorites(1, false)
            Intent.GetTopRated -> getTopRatedMovie(1, false)
            is Intent.LoadNextPopular -> getPopularFavorites(intent.page, true)
            is Intent.LoadNextTopRated -> getTopRatedMovie(intent.page, true)
            is Intent.RemoveFromFavorite -> removeFromFavorite(intent.id)
            is Intent.SaveToFavorite -> saveToFavorite(intent.movie)
            Intent.GetGenres -> getGenres()
            is Intent.GetMovieByGenre -> getListMovieByGenre(1, false, intent.withGenre)
            is Intent.GetMovieVideo -> getMovieVideo(intent.movieId)
            is Intent.GetUserReview -> getUserReview(1, intent.movieId, false)
            is Intent.LoadNextMovieByGenre -> getListMovieByGenre(intent.page, true, intent.withGenre)
            is Intent.LoadNextUserReview -> getUserReview(intent.page, intent.movieId, true)
        }
    }

    private fun getUserReview(page: Int, movieId: Int, isLoadMore: Boolean) {
        setState {
            copy(
                showLoading = true,
            )
        }

        val param = GetUserReviewUseCase.Params(page,movieId)

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getUserReviewUseCase(param)
                }
            ) {
                is Result.Success -> {
                    if (isLoadMore) {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listUserReview = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListLoadMoreUserReview
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listUserReview = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessLoadMoreUserReview
                                )
                            }
                        }
                    } else {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listUserReview = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInitUserReview
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listUserReview = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInitUserReview
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    if (isLoadMore) {
                        setState {
                            copy(
                                listUserReview = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorLoadMoreUserReview
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listUserReview = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInitUserReview
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getMovieVideo(movieId: Int) {

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getMovieVideoUseCase(movieId)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                listMovieVideo = emptyList(),
                                showLoading = false,
                                viewState = ViewState.EmptyListFirstInitMovieVideo
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listMovieVideo = result.data,
                                showLoading = false,
                                viewState = ViewState.SuccessFirstInitMovieVideo
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            listMovieVideo = emptyList(),
                            showLoading = false,
                            viewState = ViewState.ErrorFirstInitMovieVideo
                        )
                    }
                }

            }
        }
    }

    private fun getListMovieByGenre(page: Int, isLoadMore: Boolean, withGenre: Int) {
        setState {
            copy(
                showLoading = true,
            )
        }

        val param = GetMovieByGenreUseCase.Params(page,withGenre)

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getMovieByGenreUseCase(param)
                }
            ) {
                is Result.Success -> {
                    if (isLoadMore) {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listMovieByGenre = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListLoadMoreMovieByGenre
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listMovieByGenre = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessLoadMoreMovieByGenre
                                )
                            }
                        }
                    } else {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listMovieByGenre = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInitMovieByGenre
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listMovieByGenre = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInitMovieByGenre
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    if (isLoadMore) {
                        setState {
                            copy(
                                listMovieByGenre = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorLoadMoreMovieByGenre
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listMovieByGenre = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInitMovieByGenre
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getGenres() {
        setState {
            copy(
                showLoading = true,
            )
        }

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getGenreUseCase()
                }
            ) {
                is Result.Success -> {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listGenre = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInitGenre
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listGenre = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInitGenre
                                )
                            }
                        }
                }
                is Result.Error -> {
                        setState {
                            copy(
                                listGenre = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInitGenre
                            )
                        }
                }

            }
        }
    }

    private fun saveToFavorite(movie: Movie) {
        val params = InsertToFavoriteUseCase.Params(movie)
        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    insertToFavoriteUseCase(params)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listFavorite = result.data,
                                showLoading = false,
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            listFavorite = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun removeFromFavorite(id: Int) {
        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    deleteFromFavoriteUseCase(id)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listFavorite = result.data,
                                showLoading = false,
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            listFavorite = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun getTopRatedMovie(page: Int, isLoadMore: Boolean) {
        setState {
            copy(
                showLoading = true,
            )
        }

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getTopRatedUseCase(page)
                }
            ) {
                is Result.Success -> {
                    if (isLoadMore) {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listTopRated = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListLoadMore
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listTopRated = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessLoadMore
                                )
                            }
                        }
                    } else {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listTopRated = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInit
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listTopRated = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInit
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    if (isLoadMore) {
                        setState {
                            copy(
                                listTopRated = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorLoadMore
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listTopRated = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInit
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPopularMovie(page: Int, isLoadMore: Boolean, favorites: List<Movie>) {
        viewModelScope.launch {

            when (
                val result = withContext(ioDispatchers.io) {
                    getPopularUseCase(page)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isNotEmpty()) {
                        result.data.forEach { movie ->
                            favorites.forEach { favorite ->
                                if (movie.id == favorite.id) movie.isLiked = true else false
                            }
                        }
                    }

                    if (isLoadMore) {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listPopular = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListLoadMore
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listPopular = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessLoadMore
                                )
                            }
                        }
                    } else {
                        if (result.data.isEmpty()) {
                            setState {
                                copy(
                                    listPopular = emptyList(),
                                    showLoading = false,
                                    viewState = ViewState.EmptyListFirstInit
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    listPopular = result.data,
                                    showLoading = false,
                                    viewState = ViewState.SuccessFirstInit
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    if (isLoadMore) {
                        setState {
                            copy(
                                listPopular = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorLoadMore
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listPopular = emptyList(),
                                showLoading = false,
                                viewState = ViewState.ErrorFirstInit
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPopularFavorites(page: Int, isLoadMore: Boolean) {
        setState {
            copy(
                showLoading = true,
            )
        }

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getFavoritesUseCase()
                }
            ) {
                is Result.Success -> {
                    getPopularMovie(page, isLoadMore, result.data)
                    setState {
                        copy(
                            listFavorite = result.data,
                        )
                    }
                }
                is Result.Error -> {
                    getPopularMovie(page, isLoadMore, emptyList())
                    setState {
                        copy(
                            listFavorite = emptyList(),
                        )
                    }
                }
            }
        }
    }

    private fun getFavorites() {
        setState {
            copy(
                showLoading = true,
            )
        }

        viewModelScope.launch {

            when (
                val result = withContext(ioDispatchers.io) {
                    getFavoritesUseCase()
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                listFavorite = emptyList(),
                                showLoading = false,
                                viewState = ViewState.EmptyListFirstInit
                            )
                        }
                    } else {
                        setState {
                            copy(
                                listFavorite = result.data,
                                showLoading = false,
                                viewState = ViewState.SuccessFirstInit
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            listFavorite = emptyList(),
                            showLoading = false,
                            viewState = ViewState.ErrorFirstInit
                        )
                    }
                }
            }
        }
    }
}
