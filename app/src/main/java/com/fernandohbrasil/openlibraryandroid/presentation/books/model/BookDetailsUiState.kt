package com.fernandohbrasil.openlibraryandroid.presentation.books.model

import com.fernandohbrasil.openlibraryandroid.domain.books.model.BookDetails
import androidx.annotation.StringRes

data class BookDetailsUiState(
    val book: BookDetails? = null,
    val isLoading: Boolean = false,
    @StringRes val errorMessageRes: Int? = null,
)
