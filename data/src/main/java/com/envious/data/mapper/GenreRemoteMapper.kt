package com.envious.data.mapper

import com.envious.data.remote.response.GenreResponse
import com.envious.domain.model.Genre

class GenreRemoteMapper : BaseMapperRepository<GenreResponse.GenreItem, Genre> {
    override fun transform(item: GenreResponse.GenreItem): Genre = Genre(
        id = item.id,
        name = item.name
    )

    override fun transformToRepository(item: Genre): GenreResponse.GenreItem =
        GenreResponse.GenreItem(id = item.id, name = item.name)
}