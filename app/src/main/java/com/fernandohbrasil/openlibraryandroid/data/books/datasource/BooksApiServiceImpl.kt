package com.fernandohbrasil.openlibraryandroid.data.books.datasource

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BooksApiServiceImpl @Inject constructor(
    private val booksApi: BooksApi,
) : BooksApiService {

    override fun getBookDetails(
        bookKey: String,
    ): Single<NetworkBookDetails> {
        return booksApi.getBookDetails(bookKey)
    }
}
