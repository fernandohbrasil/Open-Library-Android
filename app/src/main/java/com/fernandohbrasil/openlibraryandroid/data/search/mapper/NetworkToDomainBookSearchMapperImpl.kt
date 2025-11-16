package com.fernandohbrasil.openlibraryandroid.data.search.mapper

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearch
import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapper
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import javax.inject.Inject

class NetworkToDomainBookSearchMapperImpl @Inject constructor(
    private val coverUrlMapper: CoverUrlMapper,
) : NetworkToDomainBookSearchMapper {

    override fun map(networkBookSearch: NetworkBookSearch): SearchBook {
        return SearchBook(
            key = networkBookSearch.key.removePrefix("/"),
            title = networkBookSearch.title,
            authorNames = networkBookSearch.authorName ?: emptyList(),
            coverUrl = coverUrlMapper.map(networkBookSearch.coverId).orEmpty(),
            firstPublishYear = networkBookSearch.firstPublishYear,
        )
    }
}
