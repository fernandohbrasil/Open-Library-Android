package com.fernandohbrasil.openlibraryandroid.domain.search.usecase

import androidx.paging.PagingData
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.search.repository.SearchRepository
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetBooksImplTest {

    private lateinit var sut: GetBooksImpl

    @Mock
    private lateinit var repository: SearchRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sut = GetBooksImpl(repository)
    }

    @Test
    fun invoke_should_returnObservableOfPagingData_when_repositoryReturnsSuccess() {
        val query = "harry potter"
        val pagingDataObservable = mock<Observable<PagingData<SearchBook>>>()
        whenever(repository.getBooks(query)).thenReturn(pagingDataObservable)

        val result: Observable<PagingData<SearchBook>> = sut.invoke(query)

        assertThat(result).isEqualTo(pagingDataObservable)
        verify(repository).getBooks(query)
    }

    @Test
    fun invoke_should_returnObservable_when_repositoryReturnsError() {
        val query = "test query"
        val errorObservable = Observable.error<PagingData<SearchBook>>(RuntimeException("Network error"))
        whenever(repository.getBooks(query)).thenReturn(errorObservable)

        val result: Observable<PagingData<SearchBook>> = sut.invoke(query)
        val testObserver: TestObserver<PagingData<SearchBook>> = result.test()

        testObserver.assertError(RuntimeException::class.java)
        verify(repository).getBooks(query)
    }

    @Test
    fun invoke_should_handleEmptyQuery() {
        val query = ""
        val pagingDataObservable = mock<Observable<PagingData<SearchBook>>>()
        whenever(repository.getBooks(query)).thenReturn(pagingDataObservable)

        val result: Observable<PagingData<SearchBook>> = sut.invoke(query)

        assertThat(result).isEqualTo(pagingDataObservable)
        verify(repository).getBooks(query)
    }
}
