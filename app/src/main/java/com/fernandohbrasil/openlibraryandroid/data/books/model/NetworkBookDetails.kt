package com.fernandohbrasil.openlibraryandroid.data.books.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkBookDetails(
    val key: String,
    val title: String,
    val covers: List<Long>?,
    @Json(name = "first_publish_date")
    val firstPublishDate: String?,
    val subjects: List<String>?,
    val description: Any?,
)
