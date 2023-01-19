package com.envious.data.usecase

import com.envious.domain.model.MovieVideo
import com.envious.domain.repository.MovieRepository
import com.envious.domain.usecase.BaseCaseWrapper
import com.envious.domain.util.Result
import javax.inject.Inject

class GetMovieVideoUseCase @Inject constructor(
    private val repository: MovieRepository
) : BaseCaseWrapper<List<MovieVideo>, Int>() {

    override suspend fun build(params: Int?): Result<List<MovieVideo>> {
        if (params == null) throw IllegalArgumentException("Params should not be null")
        return repository.getMovieVideo(params)
    }
}
