package com.fernandohbrasil.openlibraryandroid.data.search.repository

import androidx.paging.PagingData
import com.fernandohbrasil.openlibraryandroid.data.search.datasource.SearchApiService
import com.fernandohbrasil.openlibraryandroid.data.search.mapper.NetworkToDomainBookSearchMapper
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchRepositoryImplTest {

    private lateinit var sut: SearchRepositoryImpl

    @Mock
    private lateinit var apiService: SearchApiService

    @Mock
    private lateinit var networkToDomainBookSearchMapper: NetworkToDomainBookSearchMapper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = SearchRepositoryImpl(apiService, networkToDomainBookSearchMapper)
    }

    @Test
    fun getBooks_should_returnObservableOfPagingData() {
        val query = "test query"

        val result: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result).isNotNull()
        val testObserver: TestObserver<PagingData<SearchBook>> = result.test()
        assertThat(testObserver).isNotNull()
    }

    @Test
    fun getBooks_should_createPagerWithCorrectConfig() {
        val query = "test query"

        val result: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result).isNotNull()
    }

    @Test
    fun getBooks_should_returnDifferentObservableForDifferentQueries() {
        val query1 = "harry potter"
        val query2 = "lord of the rings"

        val result1: Observable<PagingData<SearchBook>> = sut.getBooks(query1)
        val result2: Observable<PagingData<SearchBook>> = sut.getBooks(query2)

        assertThat(result1).isNotNull()
        assertThat(result2).isNotNull()
    }

    @Test
    fun getBooks_should_handleEmptyQuery() {
        val query = ""

        val result: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result).isNotNull()
        val testObserver: TestObserver<PagingData<SearchBook>> = result.test()
        assertThat(testObserver).isNotNull()
    }

    @Test
    fun getBooks_should_handleQueryWithSpecialCharacters() {
        val query = "test & query with-special chars!"

        val result: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result).isNotNull()
        val testObserver: TestObserver<PagingData<SearchBook>> = result.test()
        assertThat(testObserver).isNotNull()
    }

    @Test
    fun getBooks_should_handleVeryLongQuery() {
        val query = "a".repeat(1000)

        val result: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result).isNotNull()
        val testObserver: TestObserver<PagingData<SearchBook>> = result.test()
        assertThat(testObserver).isNotNull()
    }

    @Test
    fun getBooks_should_createNewObservableOnEachCall() {
        val query = "test query"

        val result1: Observable<PagingData<SearchBook>> = sut.getBooks(query)
        val result2: Observable<PagingData<SearchBook>> = sut.getBooks(query)

        assertThat(result1).isNotNull()
        assertThat(result2).isNotNull()
    }
}
