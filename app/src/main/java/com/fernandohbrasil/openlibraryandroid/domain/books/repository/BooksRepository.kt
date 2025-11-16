package com.fernandohbrasil.openlibraryandroid.domain.books.repository

import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import io.reactivex.rxjava3.core.Single

interface BooksRepository {

    fun getBookDetails(bookKey: String, authorNames: List<String>? = null): Single<ApiResult<BookDetails>>
}
