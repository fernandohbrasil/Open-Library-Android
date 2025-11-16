package com.fernandohbrasil.openlibraryandroid.data.search.datasource

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearchResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchApiServiceImpl @Inject constructor(
    private val searchApi: SearchApi,
) : SearchApiService {

    override fun searchBooks(
        query: String,
        limit: Int,
        offset: Int
    ): Single<NetworkBookSearchResponse> {
        return searchApi.searchBooks(query, limit, offset)
    }
}
