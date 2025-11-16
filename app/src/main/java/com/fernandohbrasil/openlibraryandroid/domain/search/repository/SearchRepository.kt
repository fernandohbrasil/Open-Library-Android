package com.fernandohbrasil.openlibraryandroid.domain.search.repository

import androidx.paging.PagingData
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import io.reactivex.rxjava3.core.Observable

interface SearchRepository {
    fun getBooks(query: String): Observable<PagingData<SearchBook>>
}
