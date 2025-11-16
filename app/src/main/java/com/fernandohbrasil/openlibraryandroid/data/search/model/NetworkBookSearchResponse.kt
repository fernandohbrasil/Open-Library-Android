package com.fernandohbrasil.openlibraryandroid.data.search.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkBookSearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<NetworkBookSearch>,
)
