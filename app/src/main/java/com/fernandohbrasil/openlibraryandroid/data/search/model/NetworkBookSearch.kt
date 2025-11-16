package com.fernandohbrasil.openlibraryandroid.data.search.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkBookSearch(
    val key: String,
    val title: String,
    @Json(name = "author_name")
    val authorName: List<String>?,
    @Json(name = "cover_i")
    val coverId: Long?,
    @Json(name = "first_publish_year")
    val firstPublishYear: Int?,
)
