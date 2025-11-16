package com.fernandohbrasil.openlibraryandroid.domain.books.model

data class BookDetails(
    val key: String,
    val title: String,
    val authorNames: List<String>,
    val coverUrl: String,
    val firstPublishYear: String,
    val subjects: List<String>,
    val description: String,
)
