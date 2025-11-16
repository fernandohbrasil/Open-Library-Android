package com.fernandohbrasil.openlibraryandroid.data.books.mapper

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapper
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import javax.inject.Inject

class NetworkToDomainBookDetailsMapperImpl @Inject constructor(
    private val coverUrlMapper: CoverUrlMapper,
) : NetworkToDomainBookDetailsMapper {

    override fun map(networkBookDetails: NetworkBookDetails, authorNames: List<String>?): BookDetails {
        return BookDetails(
            key = networkBookDetails.key.removePrefix("/"),
            title = networkBookDetails.title,
            authorNames = authorNames.orEmpty(),
            coverUrl = coverUrlMapper.map(networkBookDetails.covers?.firstOrNull()).orEmpty(),
            firstPublishYear = networkBookDetails.firstPublishDate.orEmpty(),
            subjects = networkBookDetails.subjects ?: emptyList(),
            description = getDescription(networkBookDetails).orEmpty(),
        )
    }

    private fun getDescription(networkBookDetails: NetworkBookDetails): String? {
        return when (val desc = networkBookDetails.description) {
            is String -> desc
            is Map<*, *> -> desc["value"] as? String
            else -> null
        }
    }
}
