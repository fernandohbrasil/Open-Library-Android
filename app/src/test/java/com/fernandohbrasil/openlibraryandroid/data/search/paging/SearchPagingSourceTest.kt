package com.fernandohbrasil.openlibraryandroid.data.search.paging

import androidx.paging.PagingState
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiService
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapper
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchPagingSourceTest {

    private lateinit var sut: SearchPagingSource

    @Mock
    private lateinit var apiService: SearchApiService

    @Mock
    private lateinit var networkToDomainBookSearchMapper: NetworkToDomainBookSearchMapper

    private val query = "test query"
    private val pageSize = 20

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = SearchPagingSource(apiService, networkToDomainBookSearchMapper, query, pageSize)
    }

    @Test
    fun getRefreshKey_should_returnNull_when_noAnchorPosition() {
        val state = PagingState<Int, SearchBook>(
            pages = emptyList(),
            anchorPosition = null,
            config = androidx.paging.PagingConfig(pageSize = pageSize),
            leadingPlaceholderCount = 0,
        )

        val result = sut.getRefreshKey(state)

        assertThat(result).isNull()
    }

    @Test
    fun getRefreshKey_should_returnNull_when_anchorPositionExistsButNoPages() {
        val state = PagingState<Int, SearchBook>(
            pages = emptyList(),
            anchorPosition = 10,
            config = androidx.paging.PagingConfig(pageSize = pageSize),
            leadingPlaceholderCount = 0,
        )

        val result = sut.getRefreshKey(state)

        assertThat(result).isNull()
    }

    @Test
    fun getRefreshKey_should_returnNull_when_anchorPositionExistsButClosestPageIsNull() {
        val state = PagingState<Int, SearchBook>(
            pages = emptyList(),
            anchorPosition = 1000,
            config = androidx.paging.PagingConfig(pageSize = pageSize),
            leadingPlaceholderCount = 0,
        )

        val result = sut.getRefreshKey(state)

        assertThat(result).isNull()
    }
}
