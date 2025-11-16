package com.fernandohbrasil.openlibraryandroid.data.search.mapper

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearch
import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapper
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class NetworkToDomainBookSearchMapperImplTest {

    private lateinit var sut: NetworkToDomainBookSearchMapperImpl

    @Mock
    private lateinit var coverUrlMapper: CoverUrlMapper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = NetworkToDomainBookSearchMapperImpl(coverUrlMapper)
    }

    @Test
    fun map_should_mapBookSearchSuccessfully() {
        val networkBookSearch = NetworkBookSearch(
            key = "/works/OL123456W",
            title = "Test Book",
            authorName = listOf("Author One", "Author Two"),
            coverId = 12345L,
            firstPublishYear = 2020,
        )
        whenever(coverUrlMapper.map(12345L)).thenReturn("https://covers.openlibrary.org/b/id/12345-M.jpg")

        val result = sut.map(networkBookSearch)

        assertThat(result.key).isEqualTo("works/OL123456W")
        assertThat(result.title).isEqualTo("Test Book")
        assertThat(result.authorNames).containsExactly("Author One", "Author Two")
        assertThat(result.coverUrl).isEqualTo("https://covers.openlibrary.org/b/id/12345-M.jpg")
        assertThat(result.firstPublishYear).isEqualTo(2020)
    }

    @Test
    fun map_should_handleNullValues() {
        val networkBookSearch = NetworkBookSearch(
            key = "/works/OL123456W",
            title = "Test Book",
            authorName = null,
            coverId = null,
            firstPublishYear = null,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookSearch)

        assertThat(result.key).isEqualTo("works/OL123456W")
        assertThat(result.title).isEqualTo("Test Book")
        assertThat(result.authorNames).isEmpty()
        assertThat(result.coverUrl).isEmpty()
        assertThat(result.firstPublishYear).isNull()
    }

    @Test
    fun map_should_removePrefixFromKey() {
        val networkBookSearch = NetworkBookSearch(
            key = "/books/OL123456M",
            title = "Test Book",
            authorName = emptyList(),
            coverId = null,
            firstPublishYear = null,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookSearch)

        assertThat(result.key).isEqualTo("books/OL123456M")
        assertThat(result.key.startsWith("/")).isFalse()
    }

    @Test
    fun map_should_handleKeyWithoutPrefix() {
        val networkBookSearch = NetworkBookSearch(
            key = "works/OL123456W",
            title = "Test Book",
            authorName = emptyList(),
            coverId = null,
            firstPublishYear = null,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookSearch)

        assertThat(result.key).isEqualTo("works/OL123456W")
    }
}
