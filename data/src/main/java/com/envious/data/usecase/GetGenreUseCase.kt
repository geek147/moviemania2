package com.envious.data.usecase

import com.envious.domain.model.Genre
import com.envious.domain.model.Movie
import com.envious.domain.repository.MovieRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) : BaseCaseWrapper<List<Genre>, Unit>() {

    override suspend fun build(params: Unit?): Result<List<Genre>> {
        return repository.getGenres()
    }
}
