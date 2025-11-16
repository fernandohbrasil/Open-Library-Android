package com.fernandohbrasil.openlibraryandroid.data.search.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiService
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapper
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchPagingSource(
    private val apiService: SearchApiService,
    private val networkToDomainBookSearchMapper: NetworkToDomainBookSearchMapper,
    private val query: String,
    private val pageSize: Int,
) : RxPagingSource<Int, SearchBook>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, SearchBook>> {
        val page = params.key ?: 0
        val offset = page * pageSize

        return apiService.searchBooks(query, pageSize, offset)
            .subscribeOn(Schedulers.io())
            .map { response ->
                val books = response.docs.map { networkToDomainBookSearchMapper.map(it) }
                val nextKey = if (books.size < pageSize || (offset + books.size) >= response.numFound) {
                    null
                } else {
                    page + 1
                }
                LoadResult.Page(
                    data = books,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = nextKey,
                ) as LoadResult<Int, SearchBook>
            }
            .onErrorReturn { e -> LoadResult.Error(e) }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchBook>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
