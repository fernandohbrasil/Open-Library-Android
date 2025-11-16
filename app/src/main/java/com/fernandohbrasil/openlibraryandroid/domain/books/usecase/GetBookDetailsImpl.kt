package com.fernandohbrasil.openlibraryandroid.domain.books.usecase

import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.repository.BooksRepository
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetBookDetailsImpl @Inject constructor(
    private val booksRepository: BooksRepository,
) : GetBookDetails {

    override fun invoke(bookKey: String, authorNames: List<String>?): Single<ApiResult<BookDetails>> {
        return booksRepository.getBookDetails(bookKey, authorNames)
    }
}

