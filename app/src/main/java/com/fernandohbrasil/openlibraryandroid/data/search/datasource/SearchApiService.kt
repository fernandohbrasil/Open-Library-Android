package com.fernandohbrasil.openlibraryandroid.data.search.datasource

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearchResponse
import io.reactivex.rxjava3.core.Single

interface SearchApiService {

    fun searchBooks(
        query: String,
        limit: Int,
        offset: Int,
    ): Single<NetworkBookSearchResponse>
}
