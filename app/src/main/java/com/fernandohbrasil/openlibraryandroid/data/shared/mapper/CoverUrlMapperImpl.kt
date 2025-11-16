package com.fernandohbrasil.openlibraryandroid.data.shared.mapper

import javax.inject.Inject

class CoverUrlMapperImpl @Inject constructor() : CoverUrlMapper {

    override fun map(coverId: Long?): String? {
        if (coverId == null) return null
        return "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
    }
}


