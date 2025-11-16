package com.fernandohbrasil.openlibraryandroid.domain.search.usecase

import androidx.paging.PagingData
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import io.reactivex.rxjava3.core.Observable

interface GetBooks {
    operator fun invoke(query: String): Observable<PagingData<SearchBook>>
}
