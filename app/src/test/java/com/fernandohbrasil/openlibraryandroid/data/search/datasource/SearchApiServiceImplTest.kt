package com.fernandohbrasil.openlibraryandroid.data.search.datasource

import com.fernandohbrasil.openlibraryandroid.data.search.model.NetworkBookSearchResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchApiServiceImplTest {

    private lateinit var sut: SearchApiServiceImpl

    @Mock
    private lateinit var searchApi: SearchApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = SearchApiServiceImpl(searchApi)
    }

    @Test
    fun searchBooks_should_returnSuccess_when_apiReturnsSuccess() {
        val query = "test query"
        val limit = 20
        val offset = 0
        val networkBookSearchResponse = mock<NetworkBookSearchResponse>()
        whenever(searchApi.searchBooks(query, limit, offset)).thenReturn(Single.just(networkBookSearchResponse))

        val testObserver: TestObserver<NetworkBookSearchResponse> = sut.searchBooks(query, limit, offset).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(networkBookSearchResponse)
    }

    @Test
    fun searchBooks_should_returnError_when_apiReturnsError() {
        val query = "test query"
        val limit = 20
        val offset = 0
        val throwable = RuntimeException("Network error")
        whenever(searchApi.searchBooks(query, limit, offset)).thenReturn(Single.error(throwable))

        val testObserver: TestObserver<NetworkBookSearchResponse> = sut.searchBooks(query, limit, offset).test()

        testObserver.assertError(throwable)
        testObserver.assertNotComplete()
    }

    @Test
    fun searchBooks_should_returnError_when_runtimeException() {
        val query = "test query"
        val limit = 20
        val offset = 0
        val runtimeException = RuntimeException("Generic Error")
        whenever(searchApi.searchBooks(query, limit, offset)).thenReturn(Single.error(runtimeException))

        val testObserver: TestObserver<NetworkBookSearchResponse> = sut.searchBooks(query, limit, offset).test()

        testObserver.assertError { it.message == "Generic Error" }
        testObserver.assertNotComplete()
    }

    @Test
    fun searchBooks_should_passCorrectParameters_toApi() {
        val query = "harry potter"
        val limit = 10
        val offset = 20
        val networkBookSearchResponse = mock<NetworkBookSearchResponse>()
        whenever(searchApi.searchBooks(query, limit, offset)).thenReturn(Single.just(networkBookSearchResponse))

        val testObserver: TestObserver<NetworkBookSearchResponse> = sut.searchBooks(query, limit, offset).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(networkBookSearchResponse)
    }
}
