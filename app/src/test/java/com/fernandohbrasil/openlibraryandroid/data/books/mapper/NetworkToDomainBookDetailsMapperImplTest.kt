package com.fernandohbrasil.openlibraryandroid.data.books.mapper

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import com.fernandohbrasil.openlibraryandroid.data.shared.mapper.CoverUrlMapper
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class NetworkToDomainBookDetailsMapperImplTest {

    private lateinit var sut: NetworkToDomainBookDetailsMapperImpl

    @Mock
    private lateinit var coverUrlMapper: CoverUrlMapper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = NetworkToDomainBookDetailsMapperImpl(coverUrlMapper)
    }

    @Test
    fun map_should_mapBookDetailsSuccessfully() {
        val networkBookDetails = NetworkBookDetails(
            key = "/works/OL123456W",
            title = "Test Book",
            covers = listOf(12345L),
            firstPublishDate = "2020",
            subjects = listOf("Fiction", "Adventure"),
            description = "A test book description",
        )
        whenever(coverUrlMapper.map(12345L)).thenReturn("https://covers.openlibrary.org/b/id/12345-M.jpg")
        val authorNames = listOf("Author One", "Author Two")

        val result = sut.map(networkBookDetails, authorNames)

        assertThat(result.key).isEqualTo("works/OL123456W")
        assertThat(result.title).isEqualTo("Test Book")
        assertThat(result.authorNames).containsExactly("Author One", "Author Two")
        assertThat(result.coverUrl).isEqualTo("https://covers.openlibrary.org/b/id/12345-M.jpg")
        assertThat(result.firstPublishYear).isEqualTo("2020")
        assertThat(result.subjects).containsExactly("Fiction", "Adventure")
        assertThat(result.description).isEqualTo("A test book description")
    }

    @Test
    fun map_should_handleNullValues() {
        val networkBookDetails = NetworkBookDetails(
            key = "/works/OL123456W",
            title = "Test Book",
            covers = null,
            firstPublishDate = null,
            subjects = null,
            description = null,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookDetails, null)

        assertThat(result.key).isEqualTo("works/OL123456W")
        assertThat(result.title).isEqualTo("Test Book")
        assertThat(result.authorNames).isEmpty()
        assertThat(result.coverUrl).isEmpty()
        assertThat(result.firstPublishYear).isEmpty()
        assertThat(result.subjects).isEmpty()
        assertThat(result.description).isEmpty()
    }

    @Test
    fun map_should_handleDescriptionAsMap() {
        val networkBookDetails = NetworkBookDetails(
            key = "/works/OL123456W",
            title = "Test Book",
            covers = null,
            firstPublishDate = null,
            subjects = null,
            description = mapOf("value" to "Description from map"),
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookDetails, null)

        assertThat(result.description).isEqualTo("Description from map")
    }

    @Test
    fun map_should_handleDescriptionAsOtherType() {
        val networkBookDetails = NetworkBookDetails(
            key = "/works/OL123456W",
            title = "Test Book",
            covers = null,
            firstPublishDate = null,
            subjects = null,
            description = 12345,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookDetails, null)

        assertThat(result.description).isEmpty()
    }

    @Test
    fun map_should_removePrefixFromKey() {
        val networkBookDetails = NetworkBookDetails(
            key = "/books/OL123456M",
            title = "Test Book",
            covers = null,
            firstPublishDate = null,
            subjects = null,
            description = null,
        )
        whenever(coverUrlMapper.map(null)).thenReturn(null)

        val result = sut.map(networkBookDetails, null)

        assertThat(result.key).isEqualTo("books/OL123456M")
        assertThat(result.key.startsWith("/")).isFalse()
    }

    @Test
    fun map_should_useFirstCoverFromList() {
        val networkBookDetails = NetworkBookDetails(
            key = "/works/OL123456W",
            title = "Test Book",
            covers = listOf(11111L, 22222L, 33333L),
            firstPublishDate = null,
            subjects = null,
            description = null,
        )
        whenever(coverUrlMapper.map(11111L)).thenReturn("https://covers.openlibrary.org/b/id/11111-M.jpg")

        val result = sut.map(networkBookDetails, null)

        assertThat(result.coverUrl).isEqualTo("https://covers.openlibrary.org/b/id/11111-M.jpg")
    }
}
