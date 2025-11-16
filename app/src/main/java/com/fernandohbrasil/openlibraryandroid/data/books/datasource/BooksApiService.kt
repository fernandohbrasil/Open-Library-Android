package com.fernandohbrasil.openlibraryandroid.data.books.datasource

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import io.reactivex.rxjava3.core.Single

interface BooksApiService {

    fun getBookDetails(
        bookKey: String,
    ): Single<NetworkBookDetails>
}
