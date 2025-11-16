package com.fernandohbrasil.openlibraryandroid.domain.search.usecase

import androidx.paging.PagingData
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.search.repository.SearchRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetBooksImpl @Inject constructor(
    private val searchRepository: SearchRepository,
) : GetBooks {

    override fun invoke(query: String): Observable<PagingData<SearchBook>> {
        return searchRepository.getBooks(query)
    }
}
