package com.fernandohbrasil.openlibraryandroid.data.books.datasource

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApi {

    @GET("{olid}.json")
    fun getBookDetails(
        @Path("olid") olid: String,
    ): Single<NetworkBookDetails>
}
