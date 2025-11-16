package com.fernandohbrasil.openlibraryandroid.data.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiService
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapper
import com.fernandohbrasil.openlibraryandroid.data.search.paging.SearchPagingSource
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.search.repository.SearchRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchApiService,
    private val networkToDomainBookSearchMapper: NetworkToDomainBookSearchMapper,
) : SearchRepository {

    private companion object {
        const val PAGE_SIZE = 20
    }

    override fun getBooks(query: String): Observable<PagingData<SearchBook>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    apiService = apiService,
                    networkToDomainBookSearchMapper = networkToDomainBookSearchMapper,
                    query = query,
                    pageSize = PAGE_SIZE,
                )
            }
        ).observable
    }
}
