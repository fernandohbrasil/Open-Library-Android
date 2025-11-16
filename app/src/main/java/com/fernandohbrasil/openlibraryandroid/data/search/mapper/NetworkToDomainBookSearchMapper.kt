package com.fernandohbrasil.openlibraryandroid.data.search.mapper

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearch
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook

interface NetworkToDomainBookSearchMapper {
    fun map(networkBookSearch: NetworkBookSearch): SearchBook
}
