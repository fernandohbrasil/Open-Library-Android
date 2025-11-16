package com.fernandohbrasil.openlibraryandroid.data.shared.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CoverUrlMapperImplTest {

    private lateinit var sut: CoverUrlMapperImpl

    @Before
    fun setUp() {
        sut = CoverUrlMapperImpl()
    }

    @Test
    fun map_should_returnCoverUrl_when_coverIdIsProvided() {
        val coverId = 12345L

        val result = sut.map(coverId)

        assertThat(result).isEqualTo("https://covers.openlibrary.org/b/id/12345-M.jpg")
    }

    @Test
    fun map_should_returnNull_when_coverIdIsNull() {
        val result = sut.map(null)

        assertThat(result).isNull()
    }

    @Test
    fun map_should_returnCoverUrl_when_coverIdIsZero() {
        val coverId = 0L

        val result = sut.map(coverId)

        assertThat(result).isEqualTo("https://covers.openlibrary.org/b/id/0-M.jpg")
    }
}
