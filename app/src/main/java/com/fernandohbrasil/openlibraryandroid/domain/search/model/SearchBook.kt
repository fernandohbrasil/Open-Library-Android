package com.fernandohbrasil.openlibraryandroid.domain.search.model

data class SearchBook(
    val key: String,
    val title: String,
    val authorNames: List<String>,
    val coverUrl: String?,
    val firstPublishYear: Int?,
)
