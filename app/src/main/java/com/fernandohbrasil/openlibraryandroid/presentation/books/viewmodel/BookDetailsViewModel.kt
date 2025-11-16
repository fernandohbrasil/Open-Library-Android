package com.fernandohbrasil.openlibraryandroid.presentation.books.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import com.fernandohbrasil.openlibraryandroid.domain.books.usecase.GetBookDetails
import com.fernandohbrasil.openlibraryandroid.domain.search.model.SearchBook
import com.fernandohbrasil.openlibraryandroid.domain.shared.model.ApiResult
import com.fernandohbrasil.openlibraryandroid.presentation.books.model.BookDetailsUiState
import com.fernandohbrasil.openlibraryandroid.presentation.shared.util.toUserMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val getBookDetails: GetBookDetails,
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _bookDetailsState = MutableStateFlow(BookDetailsUiState())
    val bookDetailsState: StateFlow<BookDetailsUiState> = _bookDetailsState.asStateFlow()

    fun loadBookDetails(book: SearchBook) {
        viewModelScope.launch {
            _bookDetailsState.value = _bookDetailsState.value.copy(
                isLoading = true,
                errorMessageRes = null
            )
        }

        getBookDetails(book.key, book.authorNames)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleGetBookDetails)
            .addTo(disposables)
    }

    private fun handleGetBookDetails(result: ApiResult<BookDetails>) {
        viewModelScope.launch {
            when (result) {
                is ApiResult.Success -> handleGetBookDetailsSuccess(result)
                is ApiResult.Error -> handleGetBookDetailsError(result)
            }
        }
    }

    private fun handleGetBookDetailsSuccess(result: ApiResult.Success<BookDetails>) {
        _bookDetailsState.value = BookDetailsUiState(
            book = result.data,
            isLoading = false,
            errorMessageRes = null
        )
    }

    private fun handleGetBookDetailsError(result: ApiResult.Error) {
        _bookDetailsState.value = _bookDetailsState.value.copy(
            isLoading = false,
            errorMessageRes = result.error.toUserMessageRes()
        )
    }

    fun clearBookDetails() {
        viewModelScope.launch {
            _bookDetailsState.value = BookDetailsUiState()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

