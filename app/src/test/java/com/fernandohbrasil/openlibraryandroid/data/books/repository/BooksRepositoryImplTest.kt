package com.fernandohbrasil.openlibraryandroid.data.books.repository

import com.fernandohbrasil.openlibraryandroid.data.books.datasource.BooksApiService
import com.fernandohbrasil.openlibraryandroid.data.books.mapper.NetworkToDomainBookDetailsMapper
import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BooksRepositoryImplTest {

    private lateinit var sut: BooksRepositoryImpl

    @Mock
    private lateinit var apiService: BooksApiService

    @Mock
    private lateinit var networkToDomainBookDetailsMapper: NetworkToDomainBookDetailsMapper

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = BooksRepositoryImpl(apiService, networkToDomainBookDetailsMapper)
    }

    @Test
    fun getBookDetails_should_returnSuccess_when_apiReturnsSuccess() {
        val bookKey = "OL123456M"
        val authorNames = listOf("Author One")
        val networkBookDetails = mock<NetworkBookDetails>()
        val domainBookDetails = mock<BookDetails>()
        whenever(apiService.getBookDetails(bookKey)).thenReturn(Single.just(networkBookDetails))
        whenever(networkToDomainBookDetailsMapper.map(networkBookDetails, authorNames)).thenReturn(domainBookDetails)

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.getBookDetails(bookKey, authorNames).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(domainBookDetails)
        verify(apiService).getBookDetails(bookKey)
        verify(networkToDomainBookDetailsMapper).map(networkBookDetails, authorNames)
    }

    @Test
    fun getBookDetails_should_returnError_when_apiReturnsError() {
        val bookKey = "OL123456M"
        val authorNames = listOf("Author One")
        val throwable = RuntimeException("Network error")
        whenever(apiService.getBookDetails(bookKey)).thenReturn(Single.error(throwable))

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.getBookDetails(bookKey, authorNames).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        verify(apiService).getBookDetails(bookKey)
    }

    @Test
    fun getBookDetails_should_passNullAuthorNames_when_provided() {
        val bookKey = "OL123456M"
        val networkBookDetails = mock<NetworkBookDetails>()
        val domainBookDetails = mock<BookDetails>()
        whenever(apiService.getBookDetails(bookKey)).thenReturn(Single.just(networkBookDetails))
        whenever(networkToDomainBookDetailsMapper.map(networkBookDetails, null)).thenReturn(domainBookDetails)

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.getBookDetails(bookKey, null).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        verify(networkToDomainBookDetailsMapper).map(networkBookDetails, null)
    }
}
