package com.fernandohbrasil.openlibraryandroid.data.search.datasource

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search.json")
    fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): Single<NetworkBookSearchResponse>
}
