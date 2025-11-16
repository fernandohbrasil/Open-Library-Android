package com.fernandohbrasil.openlibraryandroid.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.search.usecase.GetBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val getBooks: GetBooks,
) : ViewModel() {

    private val _booksObservable = MutableStateFlow<Observable<PagingData<SearchBook>>?>(null)
    val booksObservable: StateFlow<Observable<PagingData<SearchBook>>?> = _booksObservable.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchBooks(query: String) {
        _booksObservable.value = getBooks(query).cachedIn(viewModelScope)
    }
}