package com.fernandohbrasil.openlibraryandroid.domain.books.usecase

import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.repository.BooksRepository
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError
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

class GetBookDetailsImplTest {

    private lateinit var sut: GetBookDetailsImpl

    @Mock
    private lateinit var repository: BooksRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = GetBookDetailsImpl(repository)
    }

    @Test
    fun invoke_should_returnSuccessResult_when_repositoryReturnsSuccess() {
        val bookKey = "OL123456M"
        val authorNames = listOf("Author One", "Author Two")
        val bookDetails = mock<BookDetails>()
        val successResult = ApiResult.Success(bookDetails)
        whenever(repository.getBookDetails(bookKey, authorNames)).thenReturn(Single.just(successResult))

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.invoke(bookKey, authorNames).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        assertThat((result as ApiResult.Success).data).isEqualTo(bookDetails)
        verify(repository).getBookDetails(bookKey, authorNames)
    }

    @Test
    fun invoke_should_returnErrorResult_when_repositoryReturnsError() {
        val bookKey = "OL123456M"
        val authorNames = listOf("Author One")
        val domainError = DomainError.Network
        val errorResult = ApiResult.Error(domainError)
        whenever(repository.getBookDetails(bookKey, authorNames)).thenReturn(Single.just(errorResult))

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.invoke(bookKey, authorNames).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Error::class.java)
        assertThat((result as ApiResult.Error).error).isEqualTo(domainError)
        verify(repository).getBookDetails(bookKey, authorNames)
    }

    @Test
    fun invoke_should_handleNullAuthorNames() {
        val bookKey = "OL123456M"
        val bookDetails = mock<BookDetails>()
        val successResult = ApiResult.Success(bookDetails)
        whenever(repository.getBookDetails(bookKey, null)).thenReturn(Single.just(successResult))

        val testObserver: TestObserver<ApiResult<BookDetails>> = sut.invoke(bookKey, null).test()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        val result = testObserver.values().first()
        assertThat(result).isInstanceOf(ApiResult.Success::class.java)
        verify(repository).getBookDetails(bookKey, null)
    }
}
