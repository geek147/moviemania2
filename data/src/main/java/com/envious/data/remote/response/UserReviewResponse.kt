package com.envious.data.remote.response


import com.squareup.moshi.Json
import androidx.annotation.Keep

@Keep
data class UserReviewResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val results: List<UserReviewItem?>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
) {
    @Keep
    data class UserReviewItem(
        @Json(name = "author")
        val author: String?,
        @Json(name = "author_details")
        val authorDetails: AuthorDetails?,
        @Json(name = "content")
        val content: String?,
        @Json(name = "created_at")
        val createdAt: String?,
        @Json(name = "id")
        val id: String,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "url")
        val url: String?
    ) {
        @Keep
        data class AuthorDetails(
            @Json(name = "avatar_path")
            val avatarPath: String?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "rating")
            val rating: Double?,
            @Json(name = "username")
            val username: String?
        )
    }
}