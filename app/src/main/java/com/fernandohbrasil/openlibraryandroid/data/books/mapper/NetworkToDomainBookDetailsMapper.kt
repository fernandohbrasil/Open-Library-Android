package com.fernandohbrasil.openlibraryandroid.data.books.mapper

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails

interface NetworkToDomainBookDetailsMapper {
    fun map(networkBookDetails: NetworkBookDetails, authorNames: List<String>? = null): BookDetails
}
