package com.fernandohbrasil.openlibraryandroid.presentation.search.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.search.usecase.GetBooks
import com.fernandohbrasil.openlibraryandroid.testutils.CoroutinesTestRule
import com.fernandohbrasil.openlibraryandroid.testutils.RxAndroidTestRule
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class BookListViewModelTest {

    @Rule
    @JvmField
    val coroutinesTestRule = CoroutinesTestRule()

    @Rule
    @JvmField
    val rxAndroidTestRule = RxAndroidTestRule()

    private lateinit var sut: BookListViewModel

    @Mock
    private lateinit var getBooks: GetBooks

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = BookListViewModel(getBooks)
    }

    @Test
    fun init_should_haveInitialState() = runTest {
        sut.booksObservable.test {
            val initialState = awaitItem()
            assertThat(initialState).isNull()
        }
    }

    @Test
    fun searchBooks_should_setObservable_when_queryIsValid() = runTest {
        val query = "harry potter"
        val pagingDataObservable = Observable.just(PagingData.empty<SearchBook>())
        whenever(getBooks(query)).thenReturn(pagingDataObservable)

        sut.booksObservable.test {
            awaitItem()

            sut.searchBooks(query)

            val updatedState = awaitItem()
            assertThat(updatedState).isNotNull()
        }
    }

    @Test
    fun searchBooks_should_handleEmptyQuery() = runTest {
        val query = ""
        val pagingDataObservable = Observable.just(PagingData.empty<SearchBook>())
        whenever(getBooks(query)).thenReturn(pagingDataObservable)

        sut.booksObservable.test {
            awaitItem()

            sut.searchBooks(query)

            val updatedState = awaitItem()
            assertThat(updatedState).isNotNull()
        }
    }

    @Test
    fun searchBooks_should_replacePreviousObservable_when_calledMultipleTimes() = runTest {
        val query1 = "harry potter"
        val query2 = "lord of the rings"
        val observable1 = Observable.just(PagingData.empty<SearchBook>())
        val observable2 = Observable.just(PagingData.empty<SearchBook>())
        whenever(getBooks(query1)).thenReturn(observable1)
        whenever(getBooks(query2)).thenReturn(observable2)

        sut.booksObservable.test {
            awaitItem() // Initial state

            sut.searchBooks(query1)
            val firstState = awaitItem()
            assertThat(firstState).isNotNull()

            sut.searchBooks(query2)
            val secondState = awaitItem()
            assertThat(secondState).isNotNull()
        }
    }

    @Test
    fun searchBooks_should_handleQueryWithSpecialCharacters() = runTest {
        val query = "test & query with-special chars!"
        val pagingDataObservable = Observable.just(PagingData.empty<SearchBook>())
        whenever(getBooks(query)).thenReturn(pagingDataObservable)

        sut.booksObservable.test {
            awaitItem()

            sut.searchBooks(query)

            val updatedState = awaitItem()
            assertThat(updatedState).isNotNull()
        }
    }

    @Test
    fun searchBooks_should_handleVeryLongQuery() = runTest {
        val query = "a".repeat(1000)
        val pagingDataObservable = Observable.just(PagingData.empty<SearchBook>())
        whenever(getBooks(query)).thenReturn(pagingDataObservable)

        sut.booksObservable.test {
            awaitItem()

            sut.searchBooks(query)

            val updatedState = awaitItem()
            assertThat(updatedState).isNotNull()
        }
    }
}
