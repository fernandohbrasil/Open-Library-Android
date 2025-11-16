package com.fernandohbrasil.openlibraryandroid.data.books.datasource

import com.fernandohbrasil.openlibraryandroid.data.books.model.NetworkBookDetails
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BooksApiServiceImplTest {

    private lateinit var sut: BooksApiServiceImpl

    @Mock
    private lateinit var booksApi: BooksApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = BooksApiServiceImpl(booksApi)
    }

    @Test
    fun getBookDetails_should_returnSuccess_when_apiReturnsSuccess() {
        val bookKey = "OL123456M"
        val networkBookDetails = mock<NetworkBookDetails>()
        whenever(booksApi.getBookDetails(bookKey)).thenReturn(Single.just(networkBookDetails))

        val testObserver: TestObserver<NetworkBookDetails> = sut.getBookDetails(bookKey).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(networkBookDetails)
    }

    @Test
    fun getBookDetails_should_returnError_when_apiReturnsError() {
        val bookKey = "OL123456M"
        val throwable = RuntimeException("Network error")
        whenever(booksApi.getBookDetails(bookKey)).thenReturn(Single.error(throwable))

        val testObserver: TestObserver<NetworkBookDetails> = sut.getBookDetails(bookKey).test()

        testObserver.assertError(throwable)
        testObserver.assertNotComplete()
    }

    @Test
    fun getBookDetails_should_returnError_when_runtimeException() {
        val bookKey = "OL123456M"
        val runtimeException = RuntimeException("Generic Error")
        whenever(booksApi.getBookDetails(bookKey)).thenReturn(Single.error(runtimeException))

        val testObserver: TestObserver<NetworkBookDetails> = sut.getBookDetails(bookKey).test()

        testObserver.assertError { it.message == "Generic Error" }
        testObserver.assertNotComplete()
    }
}
