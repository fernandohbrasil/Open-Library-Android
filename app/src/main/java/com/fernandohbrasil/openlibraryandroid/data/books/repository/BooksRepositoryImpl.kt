package com.fernandohbrasil.openlibraryandroid.data.books.repository

import com.fernandohbrasil.openlibraryandroid.data.books.datasource.BooksApiService
import com.fernandohbrasil.openlibraryandroid.data.books.mapper.NetworkToDomainBookDetailsMapper
import com.fernandohbrasil.openlibraryandroid.data.shared.toApiResult
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.repository.BooksRepository
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val apiService: BooksApiService,
    private val networkToDomainBookDetailsMapper: NetworkToDomainBookDetailsMapper,
) : BooksRepository {

    override fun getBookDetails(bookKey: String, authorNames: List<String>?): Single<ApiResult<BookDetails>> {
        return apiService.getBookDetails(bookKey)
            .map { networkToDomainBookDetailsMapper.map(it, authorNames) }
            .toApiResult()
    }
}
