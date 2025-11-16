package com.fernandohbrasil.openlibraryandroid.presentation.books.viewmodel

import app.cash.turbine.test
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.usecase.GetBookDetails
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.DomainError
import com.fernandohbrasil.openlibraryandroid.testutils.CoroutinesTestRule
import com.fernandohbrasil.openlibraryandroid.testutils.RxAndroidTestRule
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class BookDetailsViewModelTest {

    @Rule
    @JvmField
    val coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    val rxAndroidTestRule = RxAndroidTestRule()

    private lateinit var sut: BookDetailsViewModel

    @Mock
    private lateinit var getBookDetails: GetBookDetails

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = BookDetailsViewModel(getBookDetails)
    }

    @Test
    fun init_should_haveInitialState() = runTest {
        sut.bookDetailsState.test {
            val initialState = awaitItem()
            assertThat(initialState.book).isNull()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.errorMessageRes).isNull()
        }
    }

    @Test
    fun loadBookDetails_should_handleSuccess() = runTest {
        val searchBook = createSearchBook("OL123456M", "Test Book")
        val bookDetails = createBookDetails("OL123456M", "Test Book")
        val successResult = ApiResult.Success(bookDetails)
        whenever(getBookDetails(searchBook.key, searchBook.authorNames)).thenReturn(Single.just(successResult))

        sut.bookDetailsState.test {
            awaitItem() // Initial state

            sut.loadBookDetails(searchBook)
            
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.errorMessageRes).isNull()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.book).isEqualTo(bookDetails)
            assertThat(successState.errorMessageRes).isNull()
        }
    }

    @Test
    fun loadBookDetails_should_handleError() = runTest {
        val searchBook = createSearchBook("OL123456M", "Test Book")
        val domainError = DomainError.Network
        val errorResult = ApiResult.Error(domainError)
        whenever(getBookDetails(searchBook.key, searchBook.authorNames)).thenReturn(Single.just(errorResult))

        sut.bookDetailsState.test {
            awaitItem() // Initial state

            sut.loadBookDetails(searchBook)
            
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.book).isNull()
            assertThat(errorState.errorMessageRes).isNotNull()
        }
    }

    @Test
    fun loadBookDetails_should_handleNullAuthorNames() = runTest {
        val searchBook = createSearchBook("OL123456M", "Test Book", authorNames = emptyList())
        val bookDetails = createBookDetails("OL123456M", "Test Book")
        val successResult = ApiResult.Success(bookDetails)
        whenever(getBookDetails(searchBook.key, emptyList())).thenReturn(Single.just(successResult))

        sut.bookDetailsState.test {
            awaitItem() // Initial state

            sut.loadBookDetails(searchBook)

            awaitItem() // Loading state
            val successState = awaitItem()
            assertThat(successState.book).isEqualTo(bookDetails)
        }
    }

    @Test
    fun clearBookDetails_should_resetState() = runTest {
        val searchBook = createSearchBook("OL123456M", "Test Book")
        val bookDetails = createBookDetails("OL123456M", "Test Book")
        val successResult = ApiResult.Success(bookDetails)
        whenever(getBookDetails(searchBook.key, searchBook.authorNames)).thenReturn(Single.just(successResult))

        sut.bookDetailsState.test {
            awaitItem() // Initial state

            sut.loadBookDetails(searchBook)
            awaitItem() // Loading state
            awaitItem() // Success state

            sut.clearBookDetails()

            val clearedState = awaitItem()
            assertThat(clearedState.book).isNull()
            assertThat(clearedState.isLoading).isFalse()
            assertThat(clearedState.errorMessageRes).isNull()
        }
    }

    @Test
    fun loadBookDetails_should_handleHttpError() = runTest {
        val searchBook = createSearchBook("OL123456M", "Test Book")
        val domainError = DomainError.Http(code = 404, message = "Not Found")
        val errorResult = ApiResult.Error(domainError)
        whenever(getBookDetails(searchBook.key, searchBook.authorNames)).thenReturn(Single.just(errorResult))

        sut.bookDetailsState.test {
            awaitItem() // Initial state

            sut.loadBookDetails(searchBook)
            
            awaitItem() // Loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.errorMessageRes).isNotNull()
        }
    }

    private fun createSearchBook(
        key: String,
        title: String,
        authorNames: List<String> = listOf("Author One")
    ): SearchBook {
        return SearchBook(
            key = key,
            title = title,
            authorNames = authorNames,
            coverUrl = "https://covers.openlibrary.org/b/id/12345-M.jpg",
            firstPublishYear = 2020,
        )
    }

    private fun createBookDetails(
        key: String,
        title: String
    ): BookDetails {
        return BookDetails(
            key = key,
            title = title,
            authorNames = listOf("Author One"),
            coverUrl = "https://covers.openlibrary.org/b/id/12345-M.jpg",
            firstPublishYear = "2020",
            subjects = listOf("Fiction"),
            description = "A test book description",
        )
    }
}
