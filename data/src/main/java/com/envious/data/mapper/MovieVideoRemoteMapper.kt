package com.envious.data.mapper

import com.envious.data.remote.response.VideoResponse
import com.envious.domain.model.MovieVideo

class MovieVideoRemoteMapper : BaseMapperRepository<VideoResponse.MovieVideoItem, MovieVideo> {
    override fun transform(item: VideoResponse.MovieVideoItem): MovieVideo = MovieVideo(
        id = item.id,
        key = item.key.orEmpty(),
        name = item.name.orEmpty()
    )

    override fun transformToRepository(item: MovieVideo): VideoResponse.MovieVideoItem =
        VideoResponse.MovieVideoItem(
            id = item.id,
            key = item.key,
            name = item.name,
            iso6391 = "",
            iso31661 = "",
            publishedAt = "",
            official = false,
            site = "",
            size = 0,
            type = "Youtube"
        )
}