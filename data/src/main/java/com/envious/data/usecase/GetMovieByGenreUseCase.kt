package com.envious.data.usecase

import com.envious.domain.model.Movie
import com.envious.domain.repository.MovieRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetMovieByGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) : BaseCaseWrapper<List<Movie>, GetMovieByGenreUseCase.Params>() {

    override suspend fun build(params: Params?): Result<List<Movie>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")
        return repository.getMovieByGenre(params.page, params.withGenre)
    }

    class Params(val page: Int,val withGenre: Int)
}
